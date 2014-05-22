//
// $Id$

#include <steam/steam_gameserver.h>

#include "com_threerings_froth_SteamGameServerStats.h"

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamGameServerStats_setUserAchievement (
    JNIEnv* env, jclass clazz, jint userSteamId, jstring name)
{
    const char* nameChars = env->GetStringUTFChars(name, NULL);
    jboolean result = SteamGameServerStats()->SetUserAchievement(
        CSteamID((uint64)userSteamId), nameChars);
    env->ReleaseStringUTFChars(name, nameChars);
    return result;
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamGameServerStats_clearUserAchievement (
    JNIEnv* env, jclass clazz, jint userSteamId, jstring name)
{
    const char* nameChars = env->GetStringUTFChars(name, NULL);
    jboolean result = SteamGameServerStats()->ClearUserAchievement(
        CSteamID((uint64)userSteamId), nameChars);
    env->ReleaseStringUTFChars(name, nameChars);
    return result;
}
