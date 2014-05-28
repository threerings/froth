//
// $Id$

#include <steam/steam_api.h>

#include "com_threerings_froth_SteamController.h"

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamController_init (
    JNIEnv* env, jclass clazz, jstring file)
{
    const char* str = env->GetStringUTFChars(file, NULL);
    bool retval = SteamController()->Init(str);
    env->ReleaseStringUTFChars(file, str);
    return retval;
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamController_shutdown (
    JNIEnv* env, jclass clazz)
{
    return SteamController()->Shutdown();
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamController_getControllerState (
    JNIEnv* env, jclass clazz, jint controller, jobject state)
{
    // TODO: NULL should be the state. this will boo-boo-boooch
    bool retval = SteamController()->GetControllerState((uint32)controller, NULL);
    return retval;
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamController_setOverrideMode (
    JNIEnv* env, jclass clazz, jstring mode)
{
    const char* modeStr = env->GetStringUTFChars(mode, NULL);
    SteamController()->SetOverrideMode(modeStr);
    env->ReleaseStringUTFChars(mode, modeStr);
}
