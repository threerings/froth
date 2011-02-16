//
// $Id$

#include <steam_api.h>

#include "com_threerings_froth_SteamAPI.h"

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamAPI_nativeInit (
    JNIEnv* env, jclass clazz)
{
    return SteamAPI_Init();
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamAPI_shutdown (JNIEnv* env, jclass clazz)
{
    SteamAPI_Shutdown();
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamAPI_nativeIsSteamRunning (
    JNIEnv* env, jclass clazz)
{
    return SteamAPI_IsSteamRunning();
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamAPI_runCallbacks (
    JNIEnv* env, jclass clazz)
{
    SteamAPI_RunCallbacks();
}
