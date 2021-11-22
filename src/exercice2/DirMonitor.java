package exercice2;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirMonitor {

	Path path ;

	/**
	 * 	
	 * @param path
	 * @throws NotDirectoryException
	 * constructor that initiate the variable path if is readable and is a directory
	 */
	public DirMonitor(Path path) throws NotDirectoryException {
		if (Files.exists(path) && Files.isDirectory(path) && Files.isReadable(path)) {
			this.path =path;
		}else{
			throw new NotDirectoryException(path + " is not a directory");
		}

	}

	public ArrayList<Path> showAllRepositories() {
		ArrayList<Path> directoriesAndFiles = new ArrayList<Path>();
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(this.path)) {
			Iterator<Path> iterator = ds.iterator();

			while (iterator.hasNext()) {
				Path p = iterator.next();
				directoriesAndFiles.add(p);
				System.out.println(p);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return directoriesAndFiles;
	}
	
	
	//surcharge
	public void showAllRepositories(int n) throws IOException {
		
		/**
		 * appelle pour la classe interne
		
		PrefixFilter pf = new PrefixFilter(n);
		pf.accept(this.path);
		 */
		
		/**
		 * classe anonyme
		 */
		DirectoryStream.Filter<Path> pf = new DirectoryStream.Filter<Path>() {

			@Override
			public boolean accept(Path entry) throws IOException {
				try (Stream<Path> paths = Files.walk(entry)) {
					Iterator<Path> iterator = paths.iterator();

					while (iterator.hasNext()) {
						Path p = iterator.next();
						if(Files.size(p) < n) {
							System.out.println(p);
						}
					}
					return true;
				}
			}
		
		};
		
	}
	
	//2-
	//surcharge
		public void showAllRepositories2(int n) throws IOException {

			MyAction pf =new  MyAction() {
				public void applyAction(String prefix, MyAction action) throws IOException{
					
				}
				@Override
				public void perform(Path p) throws IOException {
					// TODO Auto-generated method stub
					
				}

			
			};
			
		}

	public long getFilesSize() throws IOException {
		long bytes = 0;
		try (Stream<Path> paths = Files.walk(this.path)) {
			Iterator<Path> iterator = paths.iterator();

			while (iterator.hasNext()) {
				Path p = iterator.next();
				if(Files.isRegularFile(p)) {
					bytes += Files.size(p);
				}
			}
		}

		return bytes;
	}


	/**
	 * 
	 * @return the last modified file or repository
	 */
	public Path mostRecent() {
		Path lastModifiedFileOrDir = null;
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(this.path)) {
			Iterator<Path> iterator = ds.iterator();
			
			while (iterator.hasNext()) {
				Path p = iterator.next();
				// initiate only th first time lastModifiedFileOrDir with the first file
				if(lastModifiedFileOrDir == null) {
					lastModifiedFileOrDir = p;
				}
				//loop all rep and directories till we get the most recent
				if(Files.getLastModifiedTime(p).toInstant().isAfter(Files.getLastModifiedTime(lastModifiedFileOrDir).toInstant())) {
					lastModifiedFileOrDir = p;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lastModifiedFileOrDir;

	}
	
	//classe interne
	private class PrefixFilter implements DirectoryStream.Filter<Path>{
		private int nOctet;
		public PrefixFilter(int n) {
			this.nOctet = n;
		}
		@Override
		public boolean accept(Path entry) throws IOException {
			// TODO Auto-generated method stub
			
			try (Stream<Path> paths = Files.walk(entry)) {
				Iterator<Path> iterator = paths.iterator();

				while (iterator.hasNext()) {
					Path p = iterator.next();
					if(Files.size(p) < this.nOctet) {
						System.out.println(p);
					}
				}
				return true;
			}
		}

	}

	public static void main(String[] args) throws NotDirectoryException {
		Path path = Paths.get(".");
		DirMonitor dir = new DirMonitor(path);

		dir.showAllRepositories();
		try {
			System.out.println(dir.getFilesSize());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(dir.mostRecent());
	}
}
