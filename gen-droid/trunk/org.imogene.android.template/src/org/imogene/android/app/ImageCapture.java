package org.imogene.android.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import org.imogene.android.Constants;
import org.imogene.android.Constants.Paths;
import org.imogene.android.util.file.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

public class ImageCapture extends Activity {
	
	private static final String EXTRA_PATH = "ImageCapture_path";
	
	private static final int ACTIVITY_IMAGE_CAPTURE = 1;
	
	private String mPath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			mPath = savedInstanceState.getString(EXTRA_PATH);
			return;
		}
		File tmp = new File(Paths.PATH_TEMPORARY);
		tmp.mkdirs();
		try {
			File img = File.createTempFile("tmp", ".img", tmp);
			mPath = img.getAbsolutePath();
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(img));
			startActivityForResult(intent, ACTIVITY_IMAGE_CAPTURE);
		} catch (IOException e) {
			Log.i(ImageCapture.class.getName(), "error creating temp file", e);
			setResult(RESULT_CANCELED);
			finish();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(EXTRA_PATH, mPath);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTIVITY_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			try {
				String title = UUID.randomUUID().toString();
				String description = Constants.AUTHORITY;
				String uriString = Media.insertImage(getContentResolver(), mPath, title, description);
				FileUtils.deleteFile(mPath);
				setResult(RESULT_OK, new Intent().setData(Uri.parse(uriString)));
				finish();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				FileUtils.deleteFile(mPath);
				setResult(RESULT_CANCELED);
				finish();
			}
		} else {
			FileUtils.deleteFile(mPath);
			setResult(RESULT_CANCELED);
			finish();
		}
	}
}
