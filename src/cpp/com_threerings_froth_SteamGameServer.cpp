//
// $Id$

#include <steam_gameserver.h>

#include "com_threerings_froth_SteamGameServer.h"

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
