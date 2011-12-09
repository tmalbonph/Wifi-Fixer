/*Copyright [2010-2011] [David Van de Ven]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.wahtod.wififixer.ui;

import java.lang.reflect.Method;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class FragmentSwitchboard extends Fragment {

    public static final String FRAGMENT_KEY = "FRAGMENT";
    public static final String METHOD = "newInstance";

    @SuppressWarnings("unchecked")
    public static FragmentSwitchboard newInstance(Bundle bundle) {
	try {
	    String s = bundle.getString(FRAGMENT_KEY);
	    Class c = Class.forName(s);
	    Class p[] = new Class[1];
	    p[0] = Bundle.class;
	    Method m = c.getMethod(METHOD, p);
	    return (FragmentSwitchboard) m.invoke(c, bundle);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    return new AboutFragment();
}
}