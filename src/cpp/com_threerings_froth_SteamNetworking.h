/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_threerings_froth_SteamNetworking */

#ifndef _Included_com_threerings_froth_SteamNetworking
#define _Included_com_threerings_froth_SteamNetworking
#ifdef __cplusplus
extern "C" {
#endif
#undef com_threerings_froth_SteamNetworking_MAX_UNRELIABLE_SIZE
#define com_threerings_froth_SteamNetworking_MAX_UNRELIABLE_SIZE 1200L
#undef com_threerings_froth_SteamNetworking_MAX_RELIABLE_SIZE
#define com_threerings_froth_SteamNetworking_MAX_RELIABLE_SIZE 1048576L
/* Inaccessible static: _sessionRequestCallbacks */
/* Inaccessible static: _sessionConnectCallbacks */
/*
 * Class:     com_threerings_froth_SteamNetworking
 * Method:    isP2PPacketAvailable
 * Signature: (Ljava/nio/IntBuffer;I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamNetworking_isP2PPacketAvailable
  (JNIEnv *, jclass, jobject, jint);

/*
 * Class:     com_threerings_froth_SteamNetworking
 * Method:    readP2PPacket
 * Signature: (Ljava/nio/ByteBuffer;Ljava/nio/LongBuffer;I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamNetworking_readP2PPacket
  (JNIEnv *, jclass, jobject, jobject, jint);

/*
 * Class:     com_threerings_froth_SteamNetworking
 * Method:    acceptP2PSessionWithUser
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamNetworking_acceptP2PSessionWithUser
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_threerings_froth_SteamNetworking
 * Method:    closeP2PSessionWithUser
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamNetworking_closeP2PSessionWithUser
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_threerings_froth_SteamNetworking
 * Method:    closeP2PChannelWithUser
 * Signature: (JI)Z
 */
JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamNetworking_closeP2PChannelWithUser
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     com_threerings_froth_SteamNetworking
 * Method:    nativeSendP2PPacket
 * Signature: (JLjava/nio/ByteBuffer;II)Z
 */
JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamNetworking_nativeSendP2PPacket
  (JNIEnv *, jclass, jlong, jobject, jint, jint);

/*
 * Class:     com_threerings_froth_SteamNetworking
 * Method:    addNativeSessionRequestCallback
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_threerings_froth_SteamNetworking_addNativeSessionRequestCallback
  (JNIEnv *, jclass);

/*
 * Class:     com_threerings_froth_SteamNetworking
 * Method:    addNativeSessionConnectCallback
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_threerings_froth_SteamNetworking_addNativeSessionConnectCallback
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif
