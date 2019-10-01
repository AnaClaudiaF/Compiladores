package net.unesc.compiladores.util;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class File {
	/**
	 * Selecione e lê um arquivo de um diretório.
	 */
	public static final String[] SelecionarArquivo(String as_caracter_separador) {
		return (SelecionarArquivo(0, -1).split(as_caracter_separador));
	}

	public static final String SelecionarArquivo(int an_initial_position, int an_length) {
		FileDialog lo_fd_file = new FileDialog(new Frame());

		lo_fd_file.setVisible(true);

		byte[] lh_buffer = null;

		try {

			FileInputStream lo_is_file = new FileInputStream(lo_fd_file.getDirectory() + lo_fd_file.getFile());

			try {

				if (an_length > -1) {

					if (lo_is_file.available() - an_initial_position >= an_length) {

						lh_buffer = new byte[an_length];
					}

					else {
						throw new IOException("Tamanho do arquivo \"" + lo_fd_file.getFile() + "\" menor que "
								+ ((double) (an_initial_position + an_length) / 1024) + " kb");
					}
				}

				else {

					lh_buffer = new byte[lo_is_file.available() - an_initial_position];
				}

				lo_is_file.skip(an_initial_position);

				lo_is_file.read(lh_buffer, 0, lh_buffer.length);
			} finally {

				lo_is_file.close();
			}
		} catch (FileNotFoundException ao_ex_file_not_found) {
			ao_ex_file_not_found.printStackTrace();
		} catch (IOException ao_ex_io) {
			ao_ex_io.printStackTrace();
		}

		return (new String(lh_buffer));
	}
}
