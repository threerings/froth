//
// $Id$

#include <steam/steam_gameserver.h>

#include "com_threerings_froth_SteamGameServer.h"
#include "utils.h"

class BaseCallback
{
public:

    BaseCallback (JNIEnv* env, CSteamID steamId, jobject object) :
        _env(env), _steamId(steamId), _object(env->NewGlobalRef(object))
    {}

    ~BaseCallback ()
    {
        _env->DeleteGlobalRef(_object);
    }

protected:

    void call (const char* cname, const char* method, const char* signature, ...)
    {
        jclass clazz = _env->FindClass(cname);
        jmethodID mid = _env->GetMethodID(clazz, method, signature);
        va_list args;
        va_start(args, signature);
        _env->CallVoidMethodV(_object, mid, args);
        va_end(args);
    }

    /** The Java environment pointer. */
    JNIEnv* _env;

    /** The object to call back to. */
    jobject _object;

    /** The specific Steam ID that we're looking for. */
    CSteamID _steamId;
};

class AuthenticateCallback : public BaseCallback
{
public:

    AuthenticateCallback (JNIEnv* env, CSteamID steamId, jobject object) :
        BaseCallback(env, steamId, object),
        _approveCallback(this, &AuthenticateCallback::clientApprove),
        _denyCallback(this, &AuthenticateCallback::clientDeny)
    {}

protected:

    void clientApprove (GSClientApprove_t* param)
    {
        if (param->m_SteamID == _steamId) {
            call("com/threerings/froth/SteamGameServer$NativeAuthenticateCallback",
                "clientApprove", "()V");
            delete this;
        }
    }

    void clientDeny (GSClientDeny_t* param)
    {
        if (param->m_SteamID == _steamId) {
            call("com/threerings/froth/SteamGameServer$NativeAuthenticateCallback",
                "clientDeny", "(ILjava/lang/String;)V", (jint)param->m_eDenyReason,
                _env->NewStringUTF(param->m_rgchOptionalText));
            delete this;
        }
    }

    CCallback<AuthenticateCallback, GSClientApprove_t, true> _approveCallback;
    CCallback<AuthenticateCallback, GSClientDeny_t, true> _denyCallback;
};

class AuthSessionCallback : public BaseCallback
{
public:

    AuthSessionCallback (JNIEnv* env, CSteamID steamId, jobject object) :
        BaseCallback(env, steamId, object),
        _validateCallback(this, &AuthSessionCallback::validateAuthTicketResponse)
    {}

protected:

    void validateAuthTicketResponse (ValidateAuthTicketResponse_t* param)
    {
        if (param->m_SteamID == _steamId) {
            call("com/threerings/froth/SteamGameServer$NativeAuthSessionCallback",
                "validateAuthTicketResponse", "(I)V", (jint)param->m_eAuthSessionResponse);
            delete this;
        }
    }

    CCallback<AuthSessionCallback, ValidateAuthTicketResponse_t, true> _validateCallback;
};

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamGameServer_nativeInit (
    JNIEnv* env, jclass clazz, jint ip, jshort gamePort,
    jshort queryPort, jint serverMode, jstring versionString)
{
    const char* versionStringChars = env->GetStringUTFChars(versionString, NULL);
    jboolean result = SteamGameServer_Init(
        ip, gamePort, queryPort, (EServerMode)serverMode, versionStringChars);
    env->ReleaseStringUTFChars(versionString, versionStringChars);
    return result;
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamGameServer_shutdown (
    JNIEnv* env, jclass clazz)
{
    SteamGameServer_Shutdown();
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamGameServer_runCallbacks (
    JNIEnv* env, jclass clazz)
{
    SteamGameServer_RunCallbacks();
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamGameServer_nativeBeginAuthSession (
    JNIEnv* env, jclass clazz, jobject ticket, jlong steamId, jobject callback)
{
    CSteamID steamIdObj = CSteamID((uint64)steamId);
    EBeginAuthSessionResult result = SteamGameServer()->BeginAuthSession(
        env->GetDirectBufferAddress(ticket),
        getBufferLimit(env, ticket),
        steamIdObj);
    if (result == k_EBeginAuthSessionResultOK) {
        new AuthSessionCallback(env, steamIdObj, callback);
    }
    return result;
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamGameServer_endAuthSession (
    JNIEnv* env, jclass clazz, jlong steamId)
{
    SteamGameServer()->EndAuthSession(CSteamID((uint64)steamId));
}
