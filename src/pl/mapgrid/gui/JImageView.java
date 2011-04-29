package pl.mapgrid.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import pl.mapgrid.app.Main;
import pl.mapgrid.calibration.Calibration;
import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.LatLon;
import pl.mapgrid.grid.UTMGraphics;
import pl.mapgrid.mask.MaskGrid;
import pl.mapgrid.shape.GeoShape;
import pl.mapgrid.shape.Polygon;
import pl.mapgrid.shape.Shape;
import pl.mapgrid.shape.ShapeGraphics;
import pl.mapgrid.utils.PopupListener;
import pl.mapgrid.utils.Utils;
import pl.mapgrid.utils.Utils.CoordType;
import pl.mapgrid.utils.Utils.DimentionType;

public class JImageView extends JComponent implements Observer {
	private BufferedImage image = null;
	private BufferedImage grid;
	private boolean showGrid = true;
	private final Main main;
	private boolean zoomToFit;

	public JImageView(Main main) {
		this.main = main;
		JPopupMenu menu = new JPopupMenu();
		JMenuItem item = menu.add("Pokaż cały");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleZoomToFit();
			}
		});
		addMouseListener(new PopupListener(menu));
		add(menu);
	}
	
	public Point toPoint(Calibration calibration, Coordinates c) {
		Coordinates[] calib = calibration.coordinates;
//		double lonbw  = Math.abs(calib[3].getLon() - calib[2].getLon());
//		double latrh  = Math.abs(calib[1].getLat() - calib[2].getLat());
		
		int x1 = toPoint(c, calib, 1, 0, CoordType.LON, DimentionType.WIDTH);
		int x2 = toPoint(c, calib, 2, 3, CoordType.LON, DimentionType.WIDTH);
		int y1 = toPoint(c, calib, 0, 3, CoordType.LAT, DimentionType.HEIGHT);
		int y2 = toPoint(c, calib, 1, 2, CoordType.LAT, DimentionType.HEIGHT);
		int x = (x1+x2)/2;
		int y = (y1+y2)/2;
		return new Point(x,y);
	}

	private int toPoint(Coordinates c, Coordinates[] calib, int i1, int i2, CoordType type, DimentionType dimention) {
		double v1 = Utils.get(calib[i1], type);
		double v2 = Utils.get(calib[i2], type);
		double min = Math.min(v1, v2);
		double lontw  = Math.abs(v1-v2);
		double vc = Utils.get(c, type);
		int dim = Utils.getDimention(image, dimention);
		int v;
		if(dimention == DimentionType.HEIGHT)
			v = (int) (dim - dim * (vc - min)/lontw);
		else
			v = (int) (dim*(vc - min)/lontw);
		return v;
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0,0,getWidth(), getHeight());
		if (image == null)
			return;
		int[] bounds = { image.getWidth(), image.getHeight() };
		if(zoomToFit) {
			int w = getParent().getWidth();
			int h = getParent().getHeight();
			float scaleW = (float)image.getWidth() / w;
			float scaleH = (float)image.getHeight() / h;
			float scale = Math.max(scaleW, scaleH);
			bounds[0] /= scale;
			bounds[1] /= scale;
		}else{
			bounds[0]= image.getWidth(); 
			bounds[1] = image.getHeight();
		}
		g.drawImage(image, 0, 0, bounds[0], bounds[1], this);
		if (showGrid != false && grid != null)
			g.drawImage(grid, 0, 0, bounds[0], bounds[1], this);
	}

	public boolean toggleZoomToFit() {
		zoomToFit = !zoomToFit;
		repaint();
		return zoomToFit;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;		
		setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
		repaint();
		revalidate();
	}

	public BufferedImage getImage() {
		return image;
	}
	public BufferedImage getImageWithGrid() {
		if(showGrid) {
			BufferedImage image = new BufferedImage(
					this.image.getWidth(), 
					this.image.getHeight(), 
					BufferedImage.TYPE_INT_RGB);
			Graphics g = image.getGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0,0,image.getWidth(), image.getHeight());
			g.drawImage(this.image, 0,0, this);
			g.drawImage(this.grid, 0,0, this);
			return image;
		}else{
			return image;
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("JImageView.update("+arg+")");
	}

	public void setLines(List<double[]> lines) {
		grid = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) grid.getGraphics();
		g.setColor(new Color(255, 0, 0, 128));
		g.setStroke(new BasicStroke(6));
		int x1, x2, y1, y2;
		final double d45 = Math.PI / 8;
		for (double[] c : lines) {
			if (-d45 < c[0] && c[0] < d45) {
				// vertical
				y1 = 0;
				x1 = MaskGrid.lineX(y1, c[0], c[1]);
				y2 = image.getWidth();
				x2 = MaskGrid.lineX(y2, c[0], c[1]);
				g.drawLine(x1, y1, x2, y2);
			} else {
				// horizontal
				x1 = 0;
				y1 = MaskGrid.lineY(x1, c[0], c[1]);
				x2 = image.getWidth();
				y2 = MaskGrid.lineY(x2, c[0], c[1]);
				g.drawLine(x1, y1, x2, y2);
			}
		}
		repaint();
	}

	public void toogleShowGrid() {
		showGrid = !showGrid;
		repaint();
	}

	public void setShowGrid(boolean b) {
		showGrid = b;
		repaint();
	}

	public BufferedImage getGrid() {
		return grid;
	}

	public void setGrid(List<int[]> vertical, List<int[]> horizontal,
			int firstEasting, int firstNorthing) {
		grid = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);		
		Graphics2D g = (Graphics2D) grid.getGraphics();
		UTMGraphics.Config cfg = main.utmConfig;
		UTMGraphics utmg = new UTMGraphics(g, cfg, grid.getWidth(), grid.getHeight());
		utmg.drawGrid(vertical, horizontal, firstEasting, firstNorthing);
		
		ShapeGraphics sg = new ShapeGraphics(g);
		Coordinates[] border = new LatLon[granica.length];
		for(int i=0;i<granica.length;++i) 
			border[i] = new LatLon(granica[i][1], granica[i][0]);
		Shape<Coordinates> geoshape = new Polygon<Coordinates>(border);
		Shape<Point> shape = GeoShape.project(geoshape, main.calibration, grid.getWidth(), grid.getHeight());
		g.setColor(Color.RED);
		g.setStroke(new BasicStroke(30));
		sg.draw(shape);
		setShowGrid(true);
	}
	double[][] granica = {
			{20.23002139998622,53.77802387780288},
			{20.23167140285095,53.77751523359439},
			{20.2332106576274,53.77678568390571},
			{20.2341220431828,53.77611667256431},
			{20.23468252443408,53.77515576100723},
			{20.23368624657947,53.7745691925237},
			{20.2319816144303,53.77417045029041},
			{20.23356506669051,53.77313937712552},
			{20.23471106428171,53.77288109772587},
			{20.23538690483656,53.77229821365617},
			{20.23695564537678,53.77107471174739},
			{20.2384220824817,53.77054686222854},
			{20.23979649360666,53.76998714529584},
			{20.24040879073967,53.76936165493755},
			{20.24065197161747,53.76841897608573},
			{20.24209432201235,53.76793604261923},
			{20.24290478237076,53.76598495268616},
			{20.24274122720814,53.76496644893285},
			{20.24506997344895,53.76355166316903},
			{20.24599197410029,53.76426089025609},
			{20.24734082613082,53.76458780212841},
			{20.24862779313245,53.76461974023885},
			{20.24986197729077,53.76439295167737},
			{20.25105947929336,53.7638021933007},
			{20.25154075890777,53.76264026563657},
			{20.25021429478,53.76162382687179},
			{20.24853755910805,53.76117461798174},
			{20.24875602855548,53.76062179572475},
			{20.24849897364116,53.75948076020914},
			{20.2493757637104,53.75926926398353},
			{20.2499440862595,53.75774813368564},
			{20.25077995909048,53.75764055355827},
			{20.25104210042617,53.75675506548034},
			{20.25017491943598,53.75634439033762},
			{20.24901818440926,53.75663234333177},
			{20.247918122946,53.75571961236987},
			{20.24782049638618,53.75407178630537},
			{20.24670216511743,53.75301041276785},
			{20.24539593399403,53.7518101481846},
			{20.2453669123515,53.75080302807473},
			{20.24746029713165,53.75179642903687},
			{20.25233102499063,53.75119287237145},
			{20.25451005819378,53.75014497767737},
			{20.25948374450184,53.75237309158591},
			{20.25890898405983,53.75285254293751},
			{20.25960796102505,53.75320551322131},
			{20.25930362636792,53.75362944497068},
			{20.26134681990698,53.7543903365529},
			{20.25933034949403,53.7558200923283},
			{20.25886371633614,53.75642288438456},
			{20.25884988192697,53.75706932940779},
			{20.26003995592082,53.75767501232076},
			{20.26114603555481,53.75811385435022},
			{20.26219519838892,53.75822012820588},
			{20.26290999415794,53.7579476539559},
			{20.26428602792153,53.75887695462177},
			{20.26383314873896,53.75940336059546},
			{20.26220638333585,53.76049262223118},
			{20.26245088375315,53.76161989664524},
			{20.261828793263,53.76238691808778},
			{20.26030678344343,53.76404180139351},
			{20.26070416265866,53.7648146887944},
			{20.26138826327855,53.76535377507366},
			{20.2622887631327,53.76545537367881},
			{20.26301768458572,53.7651594527477},
			{20.26285271315054,53.76478304383782},
			{20.26361538884493,53.76462585946765},
			{20.26431279708876,53.76487788926957},
			{20.26535124192064,53.7646897495385},
			{20.266028511073,53.76441132920568},
			{20.26521089766544,53.7637947385825},
			{20.26406007792613,53.76368593228855},
			{20.26331653298263,53.7632121220492},
			{20.26289439280808,53.7625803960679},
			{20.26279963659919,53.76158767437204},
			{20.2732104213276,53.7657134723153},
			{20.27749072163442,53.76939383824652},
			{20.2773988148693,53.77108941128604},
			{20.27705802108509,53.77176421484091},
			{20.27603467574334,53.77227805688062},
			{20.27433330497901,53.77206420534126},
			{20.27340170045183,53.77200743642822},
			{20.27294747673157,53.77247913847513},
			{20.27604618691503,53.77288603449954},
			{20.27609561034326,53.77335612360212},
			{20.27456053916417,53.77386970013007},
			{20.27385648508936,53.7738609986919},
			{20.27432106152797,53.77413263377584},
			{20.27610216626494,53.77399269241596},
			{20.27609094819032,53.77444663921278},
			{20.27469349703832,53.77552654543032},
			{20.27478133901585,53.77650507354519},
			{20.2757620102494,53.77686239045651},
			{20.27584272177332,53.77753419425684},
			{20.27484525316185,53.77841419343075},
			{20.27468575746003,53.77896001890853},
			{20.27339361347835,53.78021943937553},
			{20.27123840389443,53.7812845791555},
			{20.26947839979383,53.782610571237},
			{20.26824199728281,53.78390370023801},
			{20.26771539289044,53.78702412783304},
			{20.26722845271833,53.78797767252676},
			{20.26434591159688,53.7900895442101},
			{20.26210452666127,53.79053093549128},
			{20.26209906387368,53.7879867787366},
			{20.26271992346499,53.78756553736635},
			{20.26089603964072,53.78735092299262},
			{20.25985931918192,53.78663646867232},
			{20.25847924118761,53.78676206050481},
			{20.25901826519765,53.78600172946559},
			{20.25990957553489,53.78586742909098},
			{20.25927445502997,53.78525200509728},
			{20.26005403415051,53.78497505532593},
			{20.25985134429891,53.78471023718232},
			{20.25891733090259,53.78478164521579},
			{20.25822116701393,53.78374711151236},
			{20.25361830017967,53.78379137405842},
			{20.25377218861306,53.7818334129095},
			{20.25014629740316,53.78157051924083},
			{20.24906580939186,53.78206277273893},
			{20.25002628456898,53.78289438887866},
			{20.24877351105155,53.78341365442163},
			{20.24760671805073,53.78272448378633},
			{20.24675411115372,53.78304040719677},
			{20.24677173184072,53.7837440227281},
			{20.24692271036485,53.7843479234084},
			{20.24610430520668,53.78511761164047},
			{20.24456125484543,53.78484947474647},
			{20.24378138939935,53.7843923450835},
			{20.24292734436763,53.78441857827},
			{20.24303957036818,53.78492117400859},
			{20.24359562774303,53.78540108445007},
			{20.24411906819845,53.78590247679121},
			{20.24358339882135,53.78647228111417},
			{20.24227795053411,53.78689655986356},
			{20.24290360048974,53.78750372755235},
			{20.24245027369508,53.78795667602343},
			{20.24190174800124,53.78779594019466},
			{20.24095908778387,53.78933349977683},
			{20.23782011330897,53.78938069697531},
			{20.23549828855129,53.78774903767757},
			{20.23344919717303,53.78583708424225},
			{20.23219583447596,53.78516714472065},
			{20.23092220135663,53.78325969727244},
			{20.22911779735777,53.78192795989867},
			{20.22783536467041,53.78070256917275},
			{20.22751829046844,53.77960887301516},
			{20.2280686393017,53.77848221605328},
			{20.22781129477135,53.77783257159403},
			{20.23002139998622,53.77802387780288},
	};
}
