//
// $Id$

#include <steam/steam_api.h>

#include "com_threerings_froth_SteamController.h"

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamController_nInit (
    JNIEnv* env, jclass clazz, jstring file)
{
    const char* str = env->GetStringUTFChars(file, NULL);
    bool retval = SteamController()->Init(str);
    env->ReleaseStringUTFChars(file, str);
    return (jboolean)retval;
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamController_nShutdown (
    JNIEnv* env, jclass clazz)
{
    return (jboolean)SteamController()->Shutdown();
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamController_nGetControllerState (
    JNIEnv* env, jclass clazz, jint controller, jobject state)
{
    SteamControllerState_t controllerState; // on the stack
    bool retval = SteamController()->GetControllerState((uint32)controller, &controllerState);
    // we allow state to be null for our 'hasController' method
    if (retval && state != NULL) {
        // TODO: make this all pre-cached for the fasterness?
        jclass stateClazz = env->FindClass("com/threerings/froth/SteamController$State");
        jmethodID stateSet = env->GetMethodID(stateClazz, "setValues", "(IJSSSS)V");
        env->CallVoidMethod(state, stateSet,
                controllerState.unPacketNum,
                controllerState.ulButtons,
                controllerState.sLeftPadX,
                controllerState.sLeftPadY,
                controllerState.sRightPadX,
                controllerState.sRightPadY);
    }
    return (jboolean)retval;
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamController_nSetOverrideMode (
    JNIEnv* env, jclass clazz, jstring mode)
{
    const char* modeStr = env->GetStringUTFChars(mode, NULL);
    SteamController()->SetOverrideMode(modeStr);
    env->ReleaseStringUTFChars(mode, modeStr);
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamController_nTriggerHapticPulse (
    JNIEnv* env, jclass clazz, jint controller, jint pad, jshort duration)
{
    SteamController()->TriggerHapticPulse(
        (uint32)controller, (ESteamControllerPad)pad, (unsigned short)duration);
}
