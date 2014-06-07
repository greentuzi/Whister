package com.UnicornStudio.whiter.app.ServiceInterface;

import java.io.File;

public abstract class AlbumStorageDirFactory {
	public abstract File getAlbumStorageDir(String albumName);
}
