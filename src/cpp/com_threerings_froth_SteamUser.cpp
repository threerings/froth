//
// $Id$

#include <steam_api.h>

#include "com_threerings_froth_SteamUser.h"

JNIEXPORT jlong JNICALL Java_com_threerings_froth_SteamUser_getSteamID (
    JNIEnv* env, jclass clazz)
{
    return SteamUser()->GetSteamID().ConvertToUint64();
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamUser_initiateGameConnection (
    JNIEnv* env, jclass clazz, jobject authBlob, jlong gameServerSteamId,
    jint serverIp, jshort serverPort, jboolean secure)
{
    return SteamUser()->InitiateGameConnection(
        env->GetDirectBufferAddress(authBlob),
        env->GetDirectBufferCapacity(authBlob),
        CSteamID((uint64)gameServerSteamId),
        serverIp, serverPort, secure);
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamUser_terminateGameConnection (
    JNIEnv* env, jclass clazz, jint serverIp, jshort serverPort)
{
    return SteamUser()->TerminateGameConnection(serverIp, serverPort);
}
