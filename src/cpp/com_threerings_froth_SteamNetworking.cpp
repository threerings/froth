//
// $Id$

#include <steam_api.h>

#include "com_threerings_froth_SteamNetworking.h"
#include "utils.h"

class SessionRequestCallback
{
public:

    SessionRequestCallback (JNIEnv* env) :
        _env(env), _responseCallback(this, &SessionRequestCallback::p2pSessionRequest)
    {}

protected:

    STEAM_CALLBACK(SessionRequestCallback, p2pSessionRequest,
            P2PSessionRequest_t, _responseCallback) {
        jclass clazz = _env->FindClass("com/threerings/froth/SteamNetworking");
        jmethodID mid = _env->GetStaticMethodID(clazz, "p2pSessionRequest", "(J)V");
        _env->CallStaticVoidMethod(clazz, mid, (jlong)pParam->m_steamIDRemote.ConvertToUint64());
    }

    /** The Java environment pointer. */
    JNIEnv* _env;
};

class SessionConnectCallback
{
public:

    SessionConnectCallback (JNIEnv* env) :
        _env(env), _responseCallback(this, &SessionConnectCallback::p2pSessionConnectFail)
    {}

protected:

    STEAM_CALLBACK(SessionConnectCallback, p2pSessionConnectFail,
            P2PSessionConnectFail_t, _responseCallback) {
        jclass clazz = _env->FindClass("com/threerings/froth/SteamNetworking");
        jmethodID mid = _env->GetStaticMethodID(clazz, "p2pSessionConnectFail", "(JI)V");
        _env->CallStaticVoidMethod(clazz, mid,
            (jlong)pParam->m_steamIDRemote.ConvertToUint64(), (jint)pParam->m_eP2PSessionError);
    }

    /** The Java environment pointer. */
    JNIEnv* _env;
};

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamNetworking_isP2PPacketAvailable (
    JNIEnv* env, jclass clazz, jobject msgSize, jint channel)
{
    return SteamNetworking()->IsP2PPacketAvailable(
        (uint32*)env->GetDirectBufferAddress(msgSize), channel);
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamNetworking_readP2PPacket (
    JNIEnv* env, jclass clazz, jobject dest, jobject steamIdRemote, jint channel)
{
    uint32 length;
    jboolean result = SteamNetworking()->ReadP2PPacket(
        env->GetDirectBufferAddress(dest),
        env->GetDirectBufferCapacity(dest),
        &length,
        (CSteamID*)env->GetDirectBufferAddress(steamIdRemote),
        channel);
    if (result) {
        setBufferLimit(env, dest, length);
    }
    return result;
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamNetworking_acceptP2PSessionWithUser (
    JNIEnv* env, jclass clazz, jlong steamIdRemote)
{
    return SteamNetworking()->AcceptP2PSessionWithUser(CSteamID((uint64)steamIdRemote));
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamNetworking_closeP2PSessionWithUser (
    JNIEnv* env, jclass clazz, jlong steamIdRemote)
{
    return SteamNetworking()->CloseP2PSessionWithUser(CSteamID((uint64)steamIdRemote));
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamNetworking_closeP2PChannelWithUser (
    JNIEnv* env, jclass clazz, jlong steamIdRemote, jint channel)
{
    return SteamNetworking()->CloseP2PChannelWithUser(CSteamID((uint64)steamIdRemote), channel);
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamNetworking_nativeSendP2PPacket (
    JNIEnv* env, jclass clazz, jlong steamIdRemote, jobject data, jint sendType, jint channel)
{
    return SteamNetworking()->SendP2PPacket(
        CSteamID((uint64)steamIdRemote),
        env->GetDirectBufferAddress(data),
        getBufferLimit(env, data),
        (EP2PSend)sendType, channel);
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamNetworking_addNativeSessionRequestCallback (
    JNIEnv* env, jclass clazz)
{
    new SessionRequestCallback(env);
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamNetworking_addNativeSessionConnectCallback (
    JNIEnv* env, jclass clazz)
{
    new SessionConnectCallback(env);
}

