package pl.maps.tms.providers;

import pl.maps.tms.URLTileImageProvider;
import pl.maps.tms.utils.Utils;


public class GeoportalBackupImageProvider implements URLTileImageProvider {
	@Override
	public String getTileURL(int x, int y, int z) {
		String url = "file:///home/rzymek/devel/mapgrid/cache/${z}/${x}/${y}.jpg";
		return Utils.fillTileURLTemplate(url,x,y,z);
	}	
}
