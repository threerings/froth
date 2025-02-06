/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_threerings_froth_SteamUser */

#ifndef _Included_com_threerings_froth_SteamUser
#define _Included_com_threerings_froth_SteamUser
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_threerings_froth_SteamUser
 * Method:    isLoggedOn
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamUser_isLoggedOn
  (JNIEnv *, jclass);

/*
 * Class:     com_threerings_froth_SteamUser
 * Method:    getSteamID
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_threerings_froth_SteamUser_getSteamID
  (JNIEnv *, jclass);

/*
 * Class:     com_threerings_froth_SteamUser
 * Method:    startVoiceRecording
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_threerings_froth_SteamUser_startVoiceRecording
  (JNIEnv *, jclass);

/*
 * Class:     com_threerings_froth_SteamUser
 * Method:    stopVoiceRecording
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_threerings_froth_SteamUser_stopVoiceRecording
  (JNIEnv *, jclass);

/*
 * Class:     com_threerings_froth_SteamUser
 * Method:    getVoiceOptimalSampleRate
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamUser_getVoiceOptimalSampleRate
  (JNIEnv *, jclass);

/*
 * Class:     com_threerings_froth_SteamUser
 * Method:    getAuthSessionTicket
 * Signature: (Ljava/nio/ByteBuffer;)I
 */
JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamUser_getAuthSessionTicket
  (JNIEnv *, jclass, jobject);

/*
 * Class:     com_threerings_froth_SteamUser
 * Method:    cancelAuthTicket
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_threerings_froth_SteamUser_cancelAuthTicket
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_threerings_froth_SteamUser
 * Method:    addNativeSteamServerCallback
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_threerings_froth_SteamUser_addNativeSteamServerCallback
  (JNIEnv *, jclass);

/*
 * Class:     com_threerings_froth_SteamUser
 * Method:    addNativeMicroTxnCallback
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_threerings_froth_SteamUser_addNativeMicroTxnCallback
  (JNIEnv *, jclass);

/*
 * Class:     com_threerings_froth_SteamUser
 * Method:    nativeGetAvailableVoice
 * Signature: (Ljava/nio/IntBuffer;Ljava/nio/IntBuffer;I)I
 */
JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamUser_nativeGetAvailableVoice
  (JNIEnv *, jclass, jobject, jobject, jint);

/*
 * Class:     com_threerings_froth_SteamUser
 * Method:    nativeGetVoice
 * Signature: (Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;I)I
 */
JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamUser_nativeGetVoice
  (JNIEnv *, jclass, jobject, jobject, jint);

/*
 * Class:     com_threerings_froth_SteamUser
 * Method:    nativeDecompressVoice
 * Signature: (Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;I)I
 */
JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamUser_nativeDecompressVoice
  (JNIEnv *, jclass, jobject, jobject, jint);

#ifdef __cplusplus
}
#endif
#endif
