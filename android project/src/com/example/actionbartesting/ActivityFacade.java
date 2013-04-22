/**
 * Copyright 2013 Tom Renn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.example.actionbartesting;

import android.os.Bundle;

public interface ActivityFacade {
	public enum ApplicationAction {LAUNCH_MAPS, 
									LAUNCH_RATINGS,
									LAUNCH_RATINGS_COMMENT,
									LAUNCH_WEBSITES,
									LAUNCH_ORGANIZATIONS,
									LAUNCH_URL}
	
	public void perform(ApplicationAction action, Bundle data);
	public void showLoading(boolean isLoading);
	public void showLoading(int progress);

}
