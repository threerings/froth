//
// $Id$

package com.threerings.froth;

/**
 * Represents the Steam utilities interface.
 */
public class SteamUtils
{
    /** The available positions for overlay notifications. */
    public enum NotificationPosition { TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT };

    /** Controls the mode for the floating keyboard. */
    public enum FloatingGamepadTextInputMode
    {
      /** Enter dismisses the keyboard */
      SINGLE_LINE,
      /** User needs to explicitly dismiss the keyboard */
      MULTIPLE_LINES,
      /** Keyboard is displayed in a special mode that makes it easier to enter emails */
      EMAIL,
      /** Numeric keypad is shown */
      NUMERIC,
      ;
    }

    /**
     * Provides a means to obtain warning messages from the Steam API.
     */
    public interface WarningMessageHook
    {
        /**
         * Handles a warning message from Steam.
         *
         * @param severity the severity level (zero for message, one for warning).
         */
        public void warning (int severity, String message);
    }

    /**
     * Returns the application ID of the current process.
     */
    public static native int getAppID ();

    /**
     * Sets the location of the notifications on the overlay.
     */
    public static void setOverlayNotificationPosition (NotificationPosition position)
    {
        nativeSetOverlayNotificationPosition(position.ordinal());
    }

    /**
     * Sets the callback for warning messages.
     */
    public static native void setWarningMessageHook (WarningMessageHook hook);

    /**
     * Checks whether the overlay is enabled and ready for use.
     */
    public static native boolean isOverlayEnabled ();

    /**
     * Checks whether the overlay needs a call to Present (Direct3D) or SwapBuffers (OpenGL).
     */
    public static native boolean overlayNeedsPresent ();

    /**
     * The actual native overlay position set method.
     */
    protected static native void nativeSetOverlayNotificationPosition (int position);

    /**
     * Opens a floating keyboard over the game content and sends OS keyboard keys directly
     * to the game. The text field position is specified in pixels relative the origin of the
     * game window and is used to position the floating keyboard in a way that doesn't cover
     * the text field.
     *
     * @returns true if the floating keyboard was shown, otherwise, false.
     */
    public static boolean showFloatingGamepadTextInput (
      FloatingGamepadTextInputMode keyboardMode,
      int textFieldXPosition, int textFieldYPosition, int textFieldWidth, int textFieldHeight)
    {
      return nativeShowFloatingGamepadTextInput(keyboardMode.ordinal(),
          textFieldXPosition, textFieldYPosition, textFieldWidth, textFieldHeight);
    }

    protected static native boolean nativeShowFloatingGamepadTextInput (
      int keyboardMode,
      int textFieldXPosition, int textFieldYPosition, int textFieldWidth, int textFieldHeight);
}
