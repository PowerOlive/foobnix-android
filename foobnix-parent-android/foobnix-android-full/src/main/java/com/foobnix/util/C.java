/* Copyright (c) 2011 Ivan Ivanenko <ivan.ivanenko@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE. */
package com.foobnix.util;

import java.io.Serializable;

import android.content.Context;

import com.foobnix.service.MODE;

/**
 * All Foobnix Configuration Settings
 */
public class C implements Serializable {

	private transient final static String FILE_NAME = "cfg1.obj";
	private transient static C instanse = new C();

	public int[] sleepMins = { 30, 0 };
	public int[] alarmHM = { -1, -1 };
	public boolean isRandom = false;
	public MODE notificationMode = MODE.AUTO_CANCEL;

	public String supportedExts[] = new String[] { "mp3", "ogg" };

	public static C get() {
		return instanse;
	}

	public void load(Context context) {
		C read = (C) new ConfigLoader().load(context, FILE_NAME);
		if (read != null) {
			instanse = read;
		}
	}

	public void save(Context context) {
		new ConfigLoader().save(context, instanse, FILE_NAME);
	}
}

