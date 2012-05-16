package pl.maps.tms.providers;

import pl.maps.tms.utils.Utils;

public class GeoportalOrtoProvider extends GeoportalTopoProvider {
	@Override
	public String getTileURL(int x, int y, int zoom) {
		if(x < 0 || y < 0 || zoom < 0)
			return null;
		
		if(zoom < 7) {
			String layer = "ORTO_SAT";
			y = getTileCount(zoom).height - 1 - y;
			String url = "http://ars.geoportal.gov.pl/ARS/getTile.aspx?service="+layer+"&cs=EPSG2180&"
				+"fileIDX=L${z}X${x}Y${y}.jpg";
			return Utils.fillTileURLTemplate(url, x, y, zoom);
		}else{
			String layer = "ORTOFOTO";
			y = getTileCount(zoom).height - 1 - y;
			String url = "http://ars.geoportal.gov.pl/ARS/getTile.aspx?service="+layer+"&cs=EPSG2180&"
				+"fileIDX=L${z}X${x}Y${y}.jpg";
			return Utils.fillTileURLTemplate(url, x, y, zoom);			
		}
	}
	
	@Override
	public String toString() {
		return "GeoPortal ORTO";
	}

}
