/*
 * Copyright (C) 2019 Google Inc.
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

package com.example.android.devbyteviewer.util

import android.os.Handler
import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("isNetworkError", "datumlist")
fun hideIfNetworkError(view: View, isLoading: Boolean, datumlist: Any?) {
    if (isLoading) {
        view.visibility = View.VISIBLE
    } else {
        Handler().postDelayed(Runnable { view.visibility = View.INVISIBLE }, 400)

    }
}

//@BindingAdapter("imageUrl")
//fun setImageUrl(imageView: ImageView, url: String) {
//    Glide.with(imageView.context).load(url).into(imageView)
//}