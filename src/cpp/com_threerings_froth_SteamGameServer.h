/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_threerings_froth_SteamGameServer */

#ifndef _Included_com_threerings_froth_SteamGameServer
#define _Included_com_threerings_froth_SteamGameServer
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_threerings_froth_SteamGameServer
 * Method:    shutdown
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_threerings_froth_SteamGameServer_shutdown
  (JNIEnv *, jclass);

/*
 * Class:     com_threerings_froth_SteamGameServer
 * Method:    runCallbacks
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_threerings_froth_SteamGameServer_runCallbacks
  (JNIEnv *, jclass);

/*
 * Class:     com_threerings_froth_SteamGameServer
 * Method:    getSteamID
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_threerings_froth_SteamGameServer_getSteamID
  (JNIEnv *, jclass);

/*
 * Class:     com_threerings_froth_SteamGameServer
 * Method:    endAuthSession
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_threerings_froth_SteamGameServer_endAuthSession
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_threerings_froth_SteamGameServer
 * Method:    nativeInit
 * Signature: (ISSILjava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamGameServer_nativeInit
  (JNIEnv *, jclass, jint, jshort, jshort, jint, jstring);

/*
 * Class:     com_threerings_froth_SteamGameServer
 * Method:    nativeBeginAuthSession
 * Signature: (Ljava/nio/ByteBuffer;JLcom/threerings/froth/SteamGameServer/NativeAuthSessionCallback;)I
 */
JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamGameServer_nativeBeginAuthSession
  (JNIEnv *, jclass, jobject, jlong, jobject);

#ifdef __cplusplus
}
#endif
#endif
