package exercice2;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;

public class PrefixFilter implements DirectoryStream.Filter<Path>{
	private int nOctet;
	public PrefixFilter(int n) {
		this.nOctet = n;
	}
	@Override
	public boolean accept(Path entry) throws IOException {
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
