# Srec-Converter

## Srec Class

This class is used for parsing the loaded data from the input file. Can either grab the raw data or the decoded data for display. 

## Controller class

This class is where the majority of logic is stored. It is called from the Gui class to run with a source path and write path. It will load the data from the input file, parse then format it and write it to the write path. 

## Gui class

Display for the user to use. Has 2 inputs, source path and write path. On a successful run it will fill the text area with the decoded input file. 

![Alt text](https://snag.gy/3pjTiX.jpg)
