package org.run.ui.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import au.com.bytecode.opencsv.CSVReader;

@Controller
public class MainFileActionController {

	@RequestMapping("/filepick")
	public String filePick(Model model) {
		return "filepick";
	}

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public String uploadFileHandler(// @RequestParam("name") String name,
			RedirectAttributes redirectAttributes, @RequestParam("file") MultipartFile file,
			@RequestParam("startRow") Integer startRow, @RequestParam("colStartX") Integer colStartX,
			@RequestParam("colEndX") Integer colEndX, @RequestParam("colY") Integer colY) {

		if (!file.isEmpty()) {
			CSVReader reader = null;
			try {
				reader = new CSVReader(new InputStreamReader(file.getInputStream()));

				String[] nextLine;
				int i = 0;

				long startTime = System.currentTimeMillis();
				// TODO -- logger
				System.out.println("Starting time: " + startTime);

				final DecimalFormat formatter = (DecimalFormat) NumberFormat.getNumberInstance();
				final DecimalFormatSymbols s = new DecimalFormatSymbols();
				s.setExponentSeparator("e");
				formatter.setDecimalFormatSymbols(s);

				String outputStringPartial = "";

				while ((nextLine = reader.readNext()) != null && nextLine.length > 0) {
					if (nextLine.length > 1) {
						// System.out.println(Arrays.toString(nextLine));
						if (i >= startRow) {
							for (int j = 0; j < (colEndX - colStartX + 1); j++) {
								String dataString = nextLine[colStartX + j];
								Double n = formatter.parse(dataString).doubleValue();

								if (i < 100) {
									outputStringPartial = outputStringPartial.concat("  " + n);
								}
							}

							double y = parse(nextLine, colY, formatter);

							if (i < 100) {
								outputStringPartial = outputStringPartial.concat(" --- " + y + " \n ");
							}

						}
						i++;
					}
				}

				long endTime = System.currentTimeMillis();
				System.out.println("End time: " + endTime);
				
				outputStringPartial = outputStringPartial.concat("Time used: " + (endTime - startTime) + " in milli-seconds \n ");
				
				redirectAttributes.addFlashAttribute("message", outputStringPartial);
				
				return "redirect:/filepick";

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		redirectAttributes.addFlashAttribute("message", "Input file!!");

		return "redirect:/filepick";
	}

	public static double parseYValue(String[] dataRow, int colY) {
		if (dataRow != null) {
			return Double.parseDouble(dataRow[colY]);
		} else {
			// TODO log.error
			return Double.NaN;
		}
	}

	public static double parse(String[] dataRow, int colY, DecimalFormat formatter) {
		try {
			return formatter.parse(dataRow[colY]).doubleValue();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Double.NaN;
	}

}
