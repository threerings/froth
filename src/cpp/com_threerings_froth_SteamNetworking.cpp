//
// $Id$

#include <steam_api.h>

#include "com_threerings_froth_SteamNetworking.h"

/**
 * Helper function to apply a limit to the specified buffer.
 */
static void limitBuffer (JNIEnv* env, jobject buffer, uint32 limit)
{
    jclass bclazz = env->FindClass("java/nio/Buffer");
    jmethodID mid = env->GetMethodID(bclazz, "limit", "(I)Ljava/nio/Buffer;");
    env->CallObjectMethod(buffer, mid, limit);
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamNetworking_isP2PPacketAvailable (
    JNIEnv* env, jclass clazz, jobject msgSize, jint channel)
{
    return SteamNetworking()->IsP2PPacketAvailable(
        (uint32*)env->GetDirectBufferAddress(msgSize), channel);
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamNetworking_readP2PPacket (
    JNIEnv* env, jclass clazz, jobject dest, jobject steamIdRemote, jint channel)
{
    uint32 length;
    jboolean result = SteamNetworking()->ReadP2PPacket(
        env->GetDirectBufferAddress(dest),
        env->GetDirectBufferCapacity(dest),
        &length,
        (CSteamID*)env->GetDirectBufferAddress(steamIdRemote),
        channel);
    if (result) {
        limitBuffer(env, dest, length);
    }
    return result;
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamNetworking_acceptP2PSessionWithUser (
    JNIEnv* env, jclass clazz, jlong steamIdRemote)
{
    return SteamNetworking()->AcceptP2PSessionWithUser(CSteamID((uint64)steamIdRemote));
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamNetworking_closeP2PSessionWithUser (
    JNIEnv* env, jclass clazz, jlong steamIdRemote)
{
    return SteamNetworking()->CloseP2PSessionWithUser(CSteamID((uint64)steamIdRemote));
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamNetworking_closeP2PChannelWithUser (
    JNIEnv* env, jclass clazz, jlong steamIdRemote, jint channel)
{
    return SteamNetworking()->CloseP2PChannelWithUser(CSteamID((uint64)steamIdRemote), channel);
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamNetworking_nativeSendP2PPacket (
    JNIEnv* env, jclass clazz, jlong steamIdRemote, jobject data, jint sendType, jint channel)
{
    return SteamNetworking()->SendP2PPacket(
        CSteamID((uint64)steamIdRemote),
        env->GetDirectBufferAddress(data),
        env->GetDirectBufferCapacity(data),
        (EP2PSend)sendType, channel);
}

