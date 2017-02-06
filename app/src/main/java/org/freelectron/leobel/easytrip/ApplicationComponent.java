package org.freelectron.leobel.easytrip;

import org.freelectron.leobel.easytrip.fragments.InspireMeFragment;
import org.freelectron.leobel.easytrip.fragments.PinDetailsFragment;
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
        void inject(BaseActivity activity);
        void inject(HomeActivity activity);
        void inject(SearchPlaceActivity activity);

        void inject(PinListFragment fragment);
        void inject(PinDetailsFragment fragment);
}
