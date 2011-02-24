/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_threerings_froth_SteamFriends */

#ifndef _Included_com_threerings_froth_SteamFriends
#define _Included_com_threerings_froth_SteamFriends
#ifdef __cplusplus
extern "C" {
#endif
#undef com_threerings_froth_SteamFriends_FRIEND_FLAG_IMMEDIATE
#define com_threerings_froth_SteamFriends_FRIEND_FLAG_IMMEDIATE 4L
/*
 * Class:     com_threerings_froth_SteamFriends
 * Method:    getFriendCount
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamFriends_getFriendCount
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_threerings_froth_SteamFriends
 * Method:    getFriendByIndex
 * Signature: (II)J
 */
JNIEXPORT jlong JNICALL Java_com_threerings_froth_SteamFriends_getFriendByIndex
  (JNIEnv *, jclass, jint, jint);

/*
 * Class:     com_threerings_froth_SteamFriends
 * Method:    activateGameOverlayToWebPage
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_threerings_froth_SteamFriends_activateGameOverlayToWebPage
  (JNIEnv *, jclass, jstring);

#ifdef __cplusplus
}
#endif
#endif
