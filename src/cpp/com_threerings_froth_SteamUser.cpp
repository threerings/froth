//
// $Id$

#include <steam_api.h>

#include "com_threerings_froth_SteamUser.h"

/**
 * Helper function to apply a limit to the specified buffer.
 */
static void limitBuffer (JNIEnv* env, jobject buffer, uint32 limit)
{
    jclass bclazz = env->FindClass("java/nio/Buffer");
    jmethodID mid = env->GetMethodID(bclazz, "limit", "(I)Ljava/nio/Buffer;");
    env->CallObjectMethod(buffer, mid, limit);
}

JNIEXPORT jlong JNICALL Java_com_threerings_froth_SteamUser_getSteamID (
    JNIEnv* env, jclass clazz)
{
    return SteamUser()->GetSteamID().ConvertToUint64();
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamUser_initiateGameConnection (
    JNIEnv* env, jclass clazz, jobject authBlob, jlong gameServerSteamId,
    jint serverIp, jshort serverPort, jboolean secure)
{
    uint32 length = SteamUser()->InitiateGameConnection(
        env->GetDirectBufferAddress(authBlob),
        env->GetDirectBufferCapacity(authBlob),
        CSteamID((uint64)gameServerSteamId),
        serverIp, serverPort, secure);
    limitBuffer(env, authBlob, length);
    return length;
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamUser_terminateGameConnection (
    JNIEnv* env, jclass clazz, jint serverIp, jshort serverPort)
{
    return SteamUser()->TerminateGameConnection(serverIp, serverPort);
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamUser_getAuthSessionTicket (
    JNIEnv* env, jclass clazz, jobject ticket)
{
    uint32 length;
    HAuthTicket ticketId = SteamUser()->GetAuthSessionTicket(
        env->GetDirectBufferAddress(ticket),
        env->GetDirectBufferCapacity(ticket),
        &length);
    limitBuffer(env, ticket, length);
    return ticketId;
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamUser_cancelAuthTicket (
    JNIEnv* env, jclass clazz, jint ticketId)
{
    SteamUser()->CancelAuthTicket(ticketId);
}
