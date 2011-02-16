//
// $Id$

#include <steam_api.h>

#include "com_threerings_froth_SteamFriends.h"

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamFriends_activateGameOverlayToWebPage (
    JNIEnv* env, jclass clazz, jstring url)
{
    const char* str = env->GetStringUTFChars(url, NULL);
    SteamFriends()->ActivateGameOverlayToWebPage(str);
    env->ReleaseStringUTFChars(url, str);
}
