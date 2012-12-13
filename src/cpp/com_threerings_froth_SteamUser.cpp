//
// $Id$

#include <steam_api.h>
#include <stdio.h>

#include "com_threerings_froth_SteamUser.h"
#include "utils.h"

class SteamServerCallback
{
public:

    SteamServerCallback (JNIEnv* env) :
        _env(env), _connectedCallback(this, &SteamServerCallback::steamServersConnected),
            _disconnectedCallback(this, &SteamServerCallback::steamServersDisconnected)
    {}

protected:

    STEAM_CALLBACK(SteamServerCallback, steamServersConnected,
            SteamServersConnected_t, _connectedCallback) {
        jclass clazz = _env->FindClass("com/threerings/froth/SteamUser");
        jmethodID mid = _env->GetStaticMethodID(clazz, "steamServersConnected", "()V");
        _env->CallStaticVoidMethod(clazz, mid);
    }

    STEAM_CALLBACK(SteamServerCallback, steamServersDisconnected,
            SteamServersDisconnected_t, _disconnectedCallback) {
        jclass clazz = _env->FindClass("com/threerings/froth/SteamUser");
        jmethodID mid = _env->GetStaticMethodID(clazz, "steamServersDisconnected", "()V");
        _env->CallStaticVoidMethod(clazz, mid);
    }

    /** The Java environment pointer. */
    JNIEnv* _env;
};

class MicroTxnCallback
{
public:

    MicroTxnCallback (JNIEnv* env) :
        _env(env), _responseCallback(this, &MicroTxnCallback::microTxnAuthorizationResponse)
    {}

protected:

    STEAM_CALLBACK(MicroTxnCallback, microTxnAuthorizationResponse,
            MicroTxnAuthorizationResponse_t, _responseCallback) {
        printf("MicroTxnCallback AppID=%u, OrderID=%llu\n", pParam->m_unAppID, pParam->m_ulOrderID);
        fflush(stdout);
        jclass clazz = _env->FindClass("com/threerings/froth/SteamUser");
        jmethodID mid = _env->GetStaticMethodID(clazz, "microTxnAuthorizationResponse", "(IJZ)V");
        _env->CallStaticVoidMethod(clazz, mid,
            (jint)pParam->m_unAppID, (jlong)pParam->m_ulOrderID, (jboolean)pParam->m_bAuthorized);
    }

    /** The Java environment pointer. */
    JNIEnv* _env;
};

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamUser_isLoggedOn (
    JNIEnv* env, jclass clazz)
{
    return SteamUser()->BLoggedOn();
}

JNIEXPORT jlong JNICALL Java_com_threerings_froth_SteamUser_getSteamID (
    JNIEnv* env, jclass clazz)
{
    return SteamUser()->GetSteamID().ConvertToUint64();
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamUser_initiateGameConnection (
    JNIEnv* env, jclass clazz, jobject authBlob, jlong gameServerSteamId,
    jint serverIp, jshort serverPort, jboolean secure)
{
    uint32 length = SteamUser()->InitiateGameConnection(
        env->GetDirectBufferAddress(authBlob),
        env->GetDirectBufferCapacity(authBlob),
        CSteamID((uint64)gameServerSteamId),
        serverIp, serverPort, secure);
    setBufferLimit(env, authBlob, length);
    return length;
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamUser_terminateGameConnection (
    JNIEnv* env, jclass clazz, jint serverIp, jshort serverPort)
{
    return SteamUser()->TerminateGameConnection(serverIp, serverPort);
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamUser_startVoiceRecording (
    JNIEnv* env, jclass clazz)
{
    SteamUser()->StartVoiceRecording();
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamUser_stopVoiceRecording (
    JNIEnv* env, jclass clazz)
{
    SteamUser()->StopVoiceRecording();
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamUser_getVoiceOptimalSampleRate (
    JNIEnv* env, jclass clazz)
{
    return SteamUser()->GetVoiceOptimalSampleRate();
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamUser_getAuthSessionTicket (
    JNIEnv* env, jclass clazz, jobject ticket)
{
    uint32 length;
    HAuthTicket ticketId = SteamUser()->GetAuthSessionTicket(
        env->GetDirectBufferAddress(ticket),
        env->GetDirectBufferCapacity(ticket),
        &length);
    setBufferLimit(env, ticket, length);
    return ticketId;
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamUser_cancelAuthTicket (
    JNIEnv* env, jclass clazz, jint ticketId)
{
    SteamUser()->CancelAuthTicket(ticketId);
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamUser_addNativeSteamServerCallback (
    JNIEnv* env, jclass clazz)
{
    new SteamServerCallback(env);
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamUser_addNativeMicroTxnCallback (
    JNIEnv* env, jclass clazz)
{
    new MicroTxnCallback(env);
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamUser_nativeGetAvailableVoice (
    JNIEnv* env, jclass clazz, jobject compressed,
    jobject uncompressed, jint uncompressedDesiredSampleRate)
{
    return SteamUser()->GetAvailableVoice(
        (uint32*)env->GetDirectBufferAddress(compressed),
        (uint32*)env->GetDirectBufferAddress(uncompressed),
        uncompressedDesiredSampleRate);
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamUser_nativeGetVoice (
    JNIEnv* env, jclass clazz, jobject compressed,
    jobject uncompressed, jint uncompressedDesiredSampleRate)
{
    void* compressedAddress = NULL;
    uint32 compressedCapacity = 0;
    if (compressed != NULL) {
        compressedAddress = env->GetDirectBufferAddress(compressed);
        compressedCapacity = env->GetDirectBufferCapacity(compressed);
    }
    void* uncompressedAddress = NULL;
    uint32 uncompressedCapacity = 0;
    if (uncompressed != NULL) {
        uncompressedAddress = env->GetDirectBufferAddress(uncompressed);
        uncompressedCapacity = env->GetDirectBufferCapacity(uncompressed);
    }
    uint32 compressedLength, uncompressedLength;
    EVoiceResult result = SteamUser()->GetVoice(
        compressed != NULL, compressedAddress, compressedCapacity, &compressedLength,
        uncompressed != NULL, uncompressedAddress, uncompressedCapacity, &uncompressedLength,
        uncompressedDesiredSampleRate);
    if (result == k_EVoiceResultOK) {
        if (compressed != NULL) {
            setBufferLimit(env, compressed, compressedLength);
        }
        if (uncompressed != NULL) {
            setBufferLimit(env, uncompressed, uncompressedLength);
        }
    }
    return result;
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamUser_nativeDecompressVoice (
    JNIEnv* env, jclass clazz, jobject compressed, jobject dest, jint desiredSampleRate)
{
    uint32 length;
    EVoiceResult result = SteamUser()->DecompressVoice(
        env->GetDirectBufferAddress(compressed),
        getBufferLimit(env, compressed),
        env->GetDirectBufferAddress(dest),
        env->GetDirectBufferCapacity(dest),
        &length,
        desiredSampleRate);
    if (result == k_EVoiceResultOK) {
        setBufferLimit(env, dest, length);
    }
    return result;
}

