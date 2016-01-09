# IntegerString
This is a Clojure project built around big numbers and their naming. You can use this script in either Clojure, Java, or JavaScript. The source files are under `/src/io/harriknox/`.

## For Clojure
To add this project to your Clojure project, download `/src/io/harriknox/IntegerString.clj` and ensure the file is in the folder `/io/harriknox` of your project's root directory. Call `(integer-to-string %)` where `%` is an integer or a String representing an integer: returns a String of the proper reading name for the big integer. Call `(illion-group-name %)` where `%` is an integer (or String...): returns a String of the group-name associated with the group-number passed in.

## For Java
*(To be added soon)*

## For JavaScript
To add this project to your website, add `<script src="https://raw.githubusercontent.com/HarriKnox/IntegerString/master/out/main.js"></script>` to your header in your HTML file. To call it execute `io.harriknox.IntegerString.integer_to_string(n)`, where `n` is either an integer or a String representing an integer, to get the String of the proper reading name for the integer. Call `io.harriknox.IntegerString.illion_group_number(n)`, where `n` is either an integer (or String...), to get the group-name associated with the group-number passed in.
