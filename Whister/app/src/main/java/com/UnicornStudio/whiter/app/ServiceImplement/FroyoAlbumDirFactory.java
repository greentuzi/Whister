package com.UnicornStudio.whiter.app.ServiceImplement;

import android.os.Environment;

import com.UnicornStudio.whiter.app.ServiceInterface.AlbumStorageDirFactory;

import java.io.File;

public final class FroyoAlbumDirFactory extends AlbumStorageDirFactory {

	@Override
	public File getAlbumStorageDir(String albumName) {
		// TODO Auto-generated method stub
		return new File(
		  Environment.getExternalStoragePublicDirectory(
                  Environment.DIRECTORY_PICTURES
          ),
		  albumName
		);
	}
}
