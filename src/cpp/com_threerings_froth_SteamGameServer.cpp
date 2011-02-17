//
// $Id$

#include <steam_gameserver.h>

#include "com_threerings_froth_SteamGameServer.h"

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamGameServer_nativeInit (
    JNIEnv* env, jclass clazz, jint ip, jshort port, jshort gamePort,
    jshort spectatorPort, jshort queryPort, jint serverMode, jstring gameDir,
    jstring versionString)
{
    const char* gameDirChars = env->GetStringUTFChars(gameDir, NULL);
    const char* versionStringChars = env->GetStringUTFChars(versionString, NULL);
    jboolean result = SteamGameServer_Init(
        ip, port, gamePort, spectatorPort, queryPort,
        (EServerMode)serverMode, gameDirChars, versionStringChars);
    env->ReleaseStringUTFChars(gameDir, gameDirChars);
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

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamGameServer_sendUserConnectAndAuthenticate (
    JNIEnv* env, jclass clazz, jint clientIp, jobject authBlob, jobject steamId)
{
    return SteamGameServer()->SendUserConnectAndAuthenticate(
        clientIp, env->GetDirectBufferAddress(authBlob),
        env->GetDirectBufferCapacity(authBlob),
        (CSteamID*)env->GetDirectBufferAddress(steamId));
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamGameServer_sendUserDisconnect (
    JNIEnv* env, jclass clazz, jlong steamId)
{
    SteamGameServer()->SendUserDisconnect(CSteamID((uint64)steamId));
}
