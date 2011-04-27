//
// $Id$

#include <steam_api.h>

#include "com_threerings_froth_SteamFriends.h"

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
