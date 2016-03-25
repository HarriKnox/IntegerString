# Number-Name
This is a Clojure project built around numbers and their naming. You can use this script in either Clojure, Java, or JavaScript. The source files are under `/src/io/harriknox/`.

## For Clojure
To add this project to your Clojure project, download `/src/io/harriknox/NumberName.clj` and ensure the file is in the folder `/io/harriknox` of your project's root directory. Call `(number-to-string %)` where `%` is an integer or a String representing an integer: returns a String of the proper reading name for the big integer. Call `(illion-group-name %)` where `%` is an integer (or String...): returns a String of the group-name associated with the group-number passed in.

## For Java
*(To be added soon)*

## For JavaScript
To add this project to your website, add `<script src="https://cdn.rawgit.com/HarriKnox/Number-Name/86cab6ee75b0bc0f0130c7a2d46f1a5f5e4756db/out/main.js"></script>` to your header in your HTML file. To call it execute `io.harriknox.NumberName.number_to_string(n)`, where `n` is either an integer or a String representing an integer, to get the String of the proper reading name for the integer. Call `io.harriknox.NumberName.illion_group_name(n)`, where `n` is either a non-negative integer (or String...), to get the group-name associated with the group-number passed in.
*(Note that because I am using RawGit the link in the script tag is static and does not pull the latest update, so you will need to check to see if there is an update every so-often, assuming you want to keep up with the latest version. The version supplied works perfectly fine so there's no need to panic.)*
