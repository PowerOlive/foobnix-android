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
package com.foobnix.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.foobnix.util.SongUtil;
import com.foobnix.util.TimeUtil;

public class FModelBuilder extends FModel {

	public static FModelBuilder VkAudio(VkAudio vkAudio) {
		return Track(vkAudio.getArtist(), vkAudio.getTitle()).addPath(vkAudio.getUrl());

	}

	public static FModelBuilder Empty() {
		return new FModelBuilder("").addArtist("").addTitle("");
	}

	public static FModelBuilder Parent(SearchQuery searchQuery) {
		return FModelBuilder.Folder("..").addSearchQuery(searchQuery);
	}

	public static FModelBuilder Track(String artist, String title) {
		return new FModelBuilder(artist + " - " + title).addIsFile(true).addArtist(artist).addTitle(title);
	}

	public static FModelBuilder Track(de.umass.lastfm.Track track) {
		return Track(track.getArtist(), track.getName(), track.getAlbum());

	}

	public static FModelBuilder Track(String artist, String title, String album) {
		return new FModelBuilder(artist + " - " + title).addIsFile(true).addArtist(artist).addTitle(title)
		        .addAlbum(album);
	}

	public static FModelBuilder Text(String text) {
		return new FModelBuilder(text).addIsFile(true);
	}
	public static FModelBuilder PatternText(String original) {
		String text = original;
		Pattern p = Pattern.compile("^[0-9 ,.-]+");
		Matcher m = p.matcher(text);

		if (m.find()) {
			String nums = m.group(0);
			text = text.substring(nums.length());
		}

		int index = text.indexOf("-");
		String artist = "artist";
		String title = text;

		if (index > 1) {
			artist = text.substring(0, index).trim();
			title = text.substring(index + 1, text.length()).trim();
		}
		return new FModelBuilder(original).addIsFile(true).addArtist(artist).addTitle(title);
	}


	public String getNomilizedTrackNum() {
		if (getTrackNum() > 0) {
			return SongUtil.getNumWithZero(getTrackNum());
		}
		return SongUtil.getNumWithZero(getPosition() + 1);
	}

	public static FModelBuilder CreateFromVK(VkAudio song) {
		String time = TimeUtil.durationSecToString(song.getDuration());
		return Track(song.getArtist(), song.getTitle()).addDuration(time).addPath(song.getUrl());
	}

	public static FModelBuilder Folder(String text) {
		return new FModelBuilder(text).addIsFile(false);
	}

	public static FModelBuilder Search(String text, SearchQuery searchQuery) {
		return new FModelBuilder(text).addIsFile(true).addSearchQuery(searchQuery);
	}

	public static FModelBuilder SearchFolder(String text, SearchQuery searchQuery) {
		return new FModelBuilder(text).addIsFile(false).addSearchQuery(searchQuery);
	}

	private FModelBuilder(String text) {
		super(text);
	}

	public FModelBuilder addParent(String parent) {
		setParent(parent);
		return this;
	}

	public FModelBuilder addType(TYPE type) {
		setType(type);
		return this;
	}

	public FModelBuilder addTrackNum(int num) {
		setTrackNum(num);
		return this;
	}
	public FModelBuilder addDuration(String time) {
		setTime(time);
		return this;
	}

	public FModelBuilder addArtist(String artist) {
		setArtist(artist);
		return this;
	}

	
	public FModelBuilder addSearchQuery(SearchQuery searchQuery) {
		setSearchQuery(searchQuery);
		return this;
	}

	
	public FModelBuilder addTag(String tag) {
		setTag(tag);
		return this;
	}

	public FModelBuilder addAlbum(String album) {
		setAlbum(album);
		return this;
	}

	public FModelBuilder addYear(String year) {
		setYear(year);
		return this;
	}

	public FModelBuilder addTitle(String title) {
		setTitle(title);
		return this;
	}

	public FModelBuilder addIsFile(boolean isFile) {
		setFile(isFile);
		return this;
	}

	public FModelBuilder addPath(String path) {
		setPath(path);
		return this;
	}

	public FModelBuilder addExt(String ext) {
		setExt(ext);
		return this;
	}

	public FModelBuilder addSize(String size) {
		setSize(size);
		return this;
	}

	public FModelBuilder addTime(String time) {
		setTime(time);
		return this;
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
