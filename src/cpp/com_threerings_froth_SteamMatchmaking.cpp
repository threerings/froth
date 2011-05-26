//
// $Id$

#include <steam_api.h>

#include "com_threerings_froth_SteamMatchmaking.h"

class EnterLobbyCallback
{
public:

    EnterLobbyCallback (JNIEnv* env, jobject object, SteamAPICall_t call) :
        _env(env), _object(env->NewGlobalRef(object))
    {
        _result.Set(call, this, &EnterLobbyCallback::enterLobbyResponse);
    }

    ~EnterLobbyCallback ()
    {
        _env->DeleteGlobalRef(_object);
    }

protected:

    void enterLobbyResponse (LobbyEnter_t* param, bool bIOFailure)
    {
        jclass clazz = _env->FindClass("com/threerings/froth/SteamMatchmaking$EnterLobbyCallback");
        jmethodID mid = _env->GetMethodID(clazz, "enterLobbyResponse",
            "(JIZLcom/threerings/froth/SteamMatchmaking$ChatRoomEnterResponse;)V");
        jobject response = NULL;
        const char* name = getChatRoomEnterResponseName(param->m_EChatRoomEnterResponse);
        if (name != NULL) {
            jclass rclazz = _env->FindClass(
                "com/threerings/froth/SteamMatchmaking$ChatRoomEnterResponse");
            jfieldID fid = _env->GetStaticFieldID(rclazz, name,
                "Lcom/threerings/froth/SteamMatchmaking$ChatRoomEnterResponse;");
            response = _env->GetStaticObjectField(rclazz, fid);
        }
        _env->CallVoidMethod(_object, mid, (jlong)param->m_ulSteamIDLobby,
            (jint)param->m_rgfChatPermissions, (jboolean)param->m_bLocked, response);
        delete this;
    }

    const char* getChatRoomEnterResponseName (uint32 response)
    {
        switch (response) {
            case k_EChatRoomEnterResponseSuccess:
                return "SUCCESS";
            case k_EChatRoomEnterResponseDoesntExist:
                return "DOESNT_EXIST";
            case k_EChatRoomEnterResponseNotAllowed:
                return "NOT_ALLOWED";
            case k_EChatRoomEnterResponseFull:
                return "FULL";
            case k_EChatRoomEnterResponseError:
                return "ERROR";
            case k_EChatRoomEnterResponseBanned:
                return "BANNED";
            case k_EChatRoomEnterResponseLimited:
                return "LIMITED";
            case k_EChatRoomEnterResponseClanDisabled:
                return "CLAN_DISABLED";
            case k_EChatRoomEnterResponseCommunityBan:
                return "COMMUNITY_BAN";
            default:
                return NULL;
        }
    }

    /** The Java environment pointer. */
    JNIEnv* _env;

    /** The java object to call back to. */
    jobject _object;

    CCallResult<EnterLobbyCallback, LobbyEnter_t> _result;
};

class CreateLobbyCallback
{
public:

    CreateLobbyCallback (JNIEnv* env, jobject object, SteamAPICall_t call) :
        _env(env), _object(env->NewGlobalRef(object))
    {
        _result.Set(call, this, &CreateLobbyCallback::createLobbyResponse);
    }

    ~CreateLobbyCallback ()
    {
        _env->DeleteGlobalRef(_object);
    }

protected:

    void createLobbyResponse (LobbyCreated_t* param, bool bIOFailure)
    {
        jclass clazz = _env->FindClass("com/threerings/froth/SteamMatchmaking$CreateLobbyCallback");
        jmethodID mid = _env->GetMethodID(clazz, "createLobbyResponse",
            "(Lcom/threerings/froth/SteamMatchmaking$Result;J)V");
        jobject result = NULL;
        const char* name = getResultName(param->m_eResult);
        if (name != NULL) {
            jclass rclazz = _env->FindClass("com/threerings/froth/SteamMatchmaking$Result");
            jfieldID fid = _env->GetStaticFieldID(rclazz, name,
                "Lcom/threerings/froth/SteamMatchmaking$Result;");
            result = _env->GetStaticObjectField(rclazz, fid);
        }
        _env->CallVoidMethod(_object, mid, result, (jlong)param->m_ulSteamIDLobby);
        delete this;
    }

    const char* getResultName (EResult result)
    {
        switch (result) {
            case k_EResultOK:
                return "OK";
            case k_EResultNoConnection:
                return "NO_CONNECTION";
            case k_EResultTimeout:
                return "TIMEOUT";
            case k_EResultFail:
                return "FAIL";
            case k_EResultAccessDenied:
                return "ACCESS_DENIED";
            case k_EResultLimitExceeded:
                return "LIMIT_EXCEEDED";
            default:
                return NULL;
        }
    }

    /** The Java environment pointer. */
    JNIEnv* _env;

    /** The java object to call back to. */
    jobject _object;

    CCallResult<CreateLobbyCallback, LobbyCreated_t> _result;
};

class GameLobbyJoinRequestCallback
{
public:

    GameLobbyJoinRequestCallback (JNIEnv* env) :
        _env(env), _responseCallback(this, &GameLobbyJoinRequestCallback::gameLobbyJoinRequest)
    {}

protected:

    STEAM_CALLBACK(GameLobbyJoinRequestCallback, gameLobbyJoinRequest,
            GameLobbyJoinRequested_t, _responseCallback) {
        jclass clazz = _env->FindClass("com/threerings/froth/SteamMatchmaking");
        jmethodID mid = _env->GetStaticMethodID(clazz, "gameLobbyJoinRequest", "(JJ)V");
        _env->CallStaticVoidMethod(clazz, mid,
            (jlong)pParam->m_steamIDLobby.ConvertToUint64(),
            (jlong)pParam->m_steamIDFriend.ConvertToUint64());
    }

    /** The Java environment pointer. */
    JNIEnv* _env;
};

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamMatchmaking_joinLobby (
    JNIEnv* env, jclass clazz, jlong steamIdLobby, jobject callback)
{
    new EnterLobbyCallback(env, callback,
        SteamMatchmaking()->JoinLobby(CSteamID((uint64)steamIdLobby)));
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamMatchmaking_leaveLobby (
    JNIEnv* env, jclass clazz, jlong steamIdLobby)
{
    SteamMatchmaking()->LeaveLobby(CSteamID((uint64)steamIdLobby));
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamMatchmaking_inviteUserToLobby (
    JNIEnv* env, jclass clazz, jlong steamIdLobby, jlong steamIdInvitee)
{
    return SteamMatchmaking()->InviteUserToLobby(
        CSteamID((uint64)steamIdLobby), CSteamID((uint64)steamIdInvitee));
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamMatchmaking_nativeCreateLobby (
    JNIEnv* env, jclass clazz, jint type, jint maxMembers, jobject callback)
{
    new CreateLobbyCallback(env, callback,
        SteamMatchmaking()->CreateLobby((ELobbyType)type, maxMembers));
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamMatchmaking_addNativeGameLobbyJoinRequestCallback (
    JNIEnv* env, jclass clazz)
{
    new GameLobbyJoinRequestCallback(env);
}

