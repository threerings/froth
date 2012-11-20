//
// $Id$

#include <steam_api.h>

#include "com_threerings_froth_SteamFriends.h"

class GameOverlayActivationCallback
{
public:

    GameOverlayActivationCallback (JNIEnv* env) :
        _env(env), _responseCallback(this,
            &GameOverlayActivationCallback::gameOverlayActivated)
    {}

protected:

    STEAM_CALLBACK(GameOverlayActivationCallback, gameOverlayActivated,
            GameOverlayActivated_t, _responseCallback) {
        jclass clazz = _env->FindClass("com/threerings/froth/SteamFriends");
        jmethodID mid = _env->GetStaticMethodID(clazz, "gameOverlayActivated", "(Z)V");
        _env->CallStaticVoidMethod(clazz, mid, (jboolean)pParam->m_bActive);
    }

    /** The Java environment pointer. */
    JNIEnv* _env;
};

class GameRichPresenceJoinRequestCallback
{
public:

    GameRichPresenceJoinRequestCallback (JNIEnv* env) :
        _env(env), _responseCallback(this,
            &GameRichPresenceJoinRequestCallback::gameRichPresenceJoinRequested)
    {}

protected:

    STEAM_CALLBACK(GameRichPresenceJoinRequestCallback, gameRichPresenceJoinRequested,
            GameRichPresenceJoinRequested_t, _responseCallback) {
        jclass clazz = _env->FindClass("com/threerings/froth/SteamFriends");
        jmethodID mid = _env->GetStaticMethodID(clazz, "gameRichPresenceJoinRequested",
            "(JLjava/lang/String;)V");
        _env->CallStaticVoidMethod(clazz, mid,
            (jlong)pParam->m_steamIDFriend.ConvertToUint64(),
            _env->NewStringUTF(pParam->m_rgchConnect));
    }

    /** The Java environment pointer. */
    JNIEnv* _env;
};

JNIEXPORT jstring JNICALL Java_com_threerings_froth_SteamFriends_getPersonaName (
    JNIEnv* env, jclass clazz)
{
    return env->NewStringUTF(SteamFriends()->GetPersonaName());
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamFriends_getFriendCount (
    JNIEnv* env, jclass clazz, jint flags)
{
    return SteamFriends()->GetFriendCount(flags);
}

JNIEXPORT jlong JNICALL Java_com_threerings_froth_SteamFriends_getFriendByIndex
  (JNIEnv* env, jclass clazz, jint index, jint flags)
{
    return SteamFriends()->GetFriendByIndex(index, flags).ConvertToUint64();
}

JNIEXPORT jstring JNICALL Java_com_threerings_froth_SteamFriends_getFriendPersonaName (
    JNIEnv* env, jclass clazz, jlong steamId)
{
    return env->NewStringUTF(SteamFriends()->GetFriendPersonaName(CSteamID((uint64)steamId)));
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamFriends_setInGameVoiceSpeaking (
    JNIEnv* env, jclass clazz, jlong steamId, jboolean speaking)
{
    SteamFriends()->SetInGameVoiceSpeaking(CSteamID((uint64)steamId), speaking);
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamFriends_activateGameOverlayToWebPage (
    JNIEnv* env, jclass clazz, jstring url)
{
    const char* str = env->GetStringUTFChars(url, NULL);
    SteamFriends()->ActivateGameOverlayToWebPage(str);
    env->ReleaseStringUTFChars(url, str);
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamFriends_nativeActivateGameOverlayToStore (
    JNIEnv* env, jclass clazz, jint appId, jint flag)
{
    SteamFriends()->ActivateGameOverlayToStore((AppId_t)appId, (EOverlayToStoreFlag)flag);
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamFriends_setRichPresence (
    JNIEnv* env, jclass clazz, jstring key, jstring value)
{
    const char* kstr = env->GetStringUTFChars(key, NULL);
    const char* vstr = env->GetStringUTFChars(value, NULL);
    jboolean result = SteamFriends()->SetRichPresence(kstr, vstr);
    env->ReleaseStringUTFChars(key, kstr);
    env->ReleaseStringUTFChars(value, vstr);
    return result;
}

JNIEXPORT jstring JNICALL Java_com_threerings_froth_SteamFriends_getFriendRichPresence (
    JNIEnv* env, jclass clazz, jlong steamId, jstring key)
{
    const char* kstr = env->GetStringUTFChars(key, NULL);
    jstring result = env->NewStringUTF(
        SteamFriends()->GetFriendRichPresence(CSteamID((uint64)steamId), kstr));
    env->ReleaseStringUTFChars(key, kstr);
    return result;
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamFriends_inviteUserToGame (
    JNIEnv* env, jclass clazz, jlong steamIdFriend, jstring connect)
{
    const char* cstr = env->GetStringUTFChars(connect, NULL);
    jboolean result = SteamFriends()->InviteUserToGame(CSteamID((uint64)steamIdFriend), cstr);
    env->ReleaseStringUTFChars(connect, cstr);
    return result;
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamFriends_nativeGetFriendPersonaState (
    JNIEnv* env, jclass clazz, jlong steamId)
{
    return SteamFriends()->GetFriendPersonaState(CSteamID((uint64)steamId));
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamFriends_addNativeGameOverlayActivationCallback (
    JNIEnv* env, jclass clazz)
{
    new GameOverlayActivationCallback(env);
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamFriends_addNativeGameRichPresenceJoinRequestCallback (
    JNIEnv* env, jclass clazz)
{
    new GameRichPresenceJoinRequestCallback(env);
}
