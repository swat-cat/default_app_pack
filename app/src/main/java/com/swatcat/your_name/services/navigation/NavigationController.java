package com.swatcat.your_name.services.navigation;

import android.os.Bundle;

public interface NavigationController {
    void navigateTo(Screen screen, ScreenType type);
    void navigateTo(Screen screen, ScreenType type, Bundle bundle);
    Screen getActiveScreen();
}
