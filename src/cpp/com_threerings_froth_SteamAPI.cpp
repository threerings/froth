//
// $Id$

#include <jni.h>
#include <steam_api.h>

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

