/*
 * Copyright 2015 chenupt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.app.demos.layout.dragtoplayout.multiplemodel.aa;

import android.content.Context;
import android.view.View;

import com.app.demos.layout.dragtoplayout.multiplemodel.ModelListAdapter;
import com.app.demos.layout.dragtoplayout.multiplemodel.ViewManager;


/**
 * Created by chenupt@gmail.com on 2015/2/14.
 * Description : AndroidAnnotation list adapter
 */
public class AAModelListAdapter extends ModelListAdapter {

    public AAModelListAdapter(Context context, ViewManager modelManager) {
        super(context, modelManager);
    }

    @Override
    public View modelNewInstance(Context context, Class<?> owner) throws Exception {
        return (View)owner.getMethod("build", new Class[]{Context.class}).invoke(owner, context);
    }
}