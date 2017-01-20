package org.freelectron.leobel.easytrip;

import org.freelectron.leobel.easytrip.fragments.InspireMeFragment;
import org.freelectron.leobel.easytrip.fragments.PinListFragment;
import org.freelectron.leobel.easytrip.modules.AppModule;
import org.freelectron.leobel.easytrip.modules.ServicesModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by leobel on 1/4/17.
 */

@Singleton
@Component(modules = {AppModule.class, ServicesModule.class})
public interface ApplicationComponent {
        void inject(HomeActivity activity);
        void inject(MainActivity activity);
        void inject(InspireMeFragment activity);

        void inject(PinListFragment fragment);

}
