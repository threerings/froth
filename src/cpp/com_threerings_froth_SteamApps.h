/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_threerings_froth_SteamApps */

#ifndef _Included_com_threerings_froth_SteamApps
#define _Included_com_threerings_froth_SteamApps
#ifdef __cplusplus
extern "C" {
#endif
/* Inaccessible static: _dlcInstalledCallbacks */
/*
 * Class:     com_threerings_froth_SteamApps
 * Method:    getCurrentGameLanguage
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_threerings_froth_SteamApps_getCurrentGameLanguage
  (JNIEnv *, jclass);

/*
 * Class:     com_threerings_froth_SteamApps
 * Method:    isDlcInstalled
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamApps_isDlcInstalled
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_threerings_froth_SteamApps
 * Method:    addNativeDlcInstalledCallback
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_threerings_froth_SteamApps_addNativeDlcInstalledCallback
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif
