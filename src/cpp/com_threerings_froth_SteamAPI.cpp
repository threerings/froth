//
// $Id: ProjectXStats.java 11555 2011-01-04 21:16:12Z andrzej $

#include <jni.h>

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamAPI_Init (JNIEnv* env, jclass clazz)
{
    return JNI_TRUE;
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamAPI_Shutdown (JNIEnv* env, jclass clazz)
{
}
