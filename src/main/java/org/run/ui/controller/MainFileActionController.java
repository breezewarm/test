package org.run.ui.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import au.com.bytecode.opencsv.CSVReader;

@Controller
public class MainFileActionController {

	@RequestMapping("/filepick")
    public String filePick(Model model) {
        return "filepick";
    }
	
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public String uploadFileHandler(//@RequestParam("name") String name,
            @RequestParam("file") MultipartFile file,
            @RequestParam("colStartX") Integer colStartX,
            @RequestParam("colEndX") Integer colEndX, 
            @RequestParam("colY") Integer colY) {
 
      if (!file.isEmpty()) {
		CSVReader reader = null;
		try {
			reader = new CSVReader(new InputStreamReader(file.getInputStream()));

			String[] nextLine;
			int i = 0;

			long startTime = System.currentTimeMillis();
			//TODO -- logger
			System.out.println("Starting time: " + startTime);

			while ((nextLine = reader.readNext()) != null
					&& nextLine.length > 0) {
				i++;

				System.out.println(Arrays.toString(nextLine));
				if (i % 10000 == 0) {
					long currTime = System.currentTimeMillis();
					//TODO -- logger.debug
					System.out
							.println("processed line: " + i
									+ " current time passed: "
									+ (currTime - startTime));
				}
			}

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
		return "greeting";
    }

}
