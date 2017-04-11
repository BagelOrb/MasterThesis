package probeersels;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;


public class convertOutputForLatex {

	static File baseOutput = new File("C:\\Users\\TK\\Documents\\Thesis\\Thesis Text\\Thesis Chapters\\");
	
	public static void main(String[] args) {

		
		
//		File folder = new File("C:\\Users\\TK\\Documents\\Thesis\\Results\\U 7 7");
//		File toFolder = new File("Experimentation/Images/U_7_7");
//		File folder = new File("C:\\Users\\TK\\Documents\\Thesis\\Results\\U 7 2");
//		File toFolder = new File("Experimentation/Images/U_7_2");
//		File folder = new File("C:\\Users\\TK\\Documents\\Thesis\\Results\\CAE 7 7");
//		File toFolder = new File("Experimentation/Images/CAE_7_7");
//		File folder = new File("C:\\Users\\TK\\Documents\\Thesis\\Results\\CAE 7 2");
//		File toFolder = new File("Experimentation/Images/CAE_7_2");
//		File folder = new File("C:\\Users\\TK\\Documents\\Thesis\\Results\\EED72");
//		File toFolder = new File("Experimentation/Images/EED72");
//		File folder = new File("C:\\Users\\TK\\Documents\\Thesis\\Results\\EED77");
//		File toFolder = new File("Experimentation/Images/EED77");
//		File folder = new File("C:\\Users\\TK\\Documents\\Thesis\\Results\\PSG72");
//		File toFolder = new File("Experimentation/Images/PSG72");
		File folder = new File("C:\\Users\\TK\\Documents\\Thesis\\Results\\PSG77");
		File toFolder = new File("Experimentation/Images/PSG77");
		
		boolean gray = true;
		
		
		String type = (gray)? "Gray" : "Color";
		
		StringBuilder texInclude = new StringBuilder();
		texInclude.append("\\begin{figure}[ht]\r\n\\begin{center}\r\n\\newcommand*{\\w}{.5cm}\r\n\\setlength\\arraycolsep{2pt}\r\n");
		texInclude.append("$\r\n\\begin{array}{l cccccccccc cccccccccc}\r\n");
		
		int run = 1;
		for (File subFolder : folder.listFiles())
		{
			if (!subFolder.isDirectory())
				continue;
			
			for (File subSubFolder : subFolder.listFiles())
			{
				if (!subSubFolder.isDirectory())
					continue;
				if (subSubFolder.getName().contains("layerClass"))
					continue;
				
				texInclude.append(run+" & \\input{");
				run++;
				
				StringBuilder imgsTex = new StringBuilder();
//				imgsTex.append("$\r\n\\begin{array}{cccccccccc cccccccccc}\r\n");
				
				
				String newFolder = toFolder+"/"+subFolder.getName()+"/"+subSubFolder.getName();
				
				
				String[] texIncludes = new String[20];
				boolean first = true;
				for (File img : subSubFolder.listFiles())
				{
					if ( ! FilenameUtils.getExtension(img.getPath()).equalsIgnoreCase("png"))
						continue;
					if (img.getName().contains("gray") ^ gray)
						continue;
					if (img.getName().contains("img"))
						continue;
					if ( ! img.getName().startsWith("feature"))
						continue;
					
					String num = img.getName().substring(7, 9);
					if (num.endsWith("_"))
						num = num.substring(0, 1);
					int f = Integer.parseInt(num);

					
					
					String newImgFile = newFolder+"/"+type+f+".png";
					try {
						FileUtils.copyFile(img, new File(baseOutput+"/"+newImgFile));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					texIncludes[f] = "\\includegraphics[width=\\w]{"+newImgFile.replace('\\', '/')+"}";
				}
				
				imgsTex.append(StringUtils.join(texIncludes, " &\r\n"));
//				imgsTex.append("\r\n\\end{array}$");
				
				String imgsTexFile = newFolder+"/imgs"+type+".tex";
				try {
					FileUtils.write(new File(baseOutput+"/"+imgsTexFile), imgsTex);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				texInclude.append(imgsTexFile.replace('\\', '/') + "}  \\\\  % "+subFolder.getName()+" \r\n"); //   \\vspace{5pt}\r\n");
				
			}
			
		}
		
		texInclude.append("\r\n\\end{array}$");
		texInclude.append("\\end{center}\r\n\\caption{Scaled weight images of all runs of PCAEs with convolution field size  $(7\\times 7)$ and pool size $(2\\times 2)$.}\r\n\\end{figure}");

		
		System.out.println(texInclude);
	}
}
