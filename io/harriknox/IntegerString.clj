(ns io.harriknox.IntegerString
    (:require [clojure.string]))

(def number-names ^:private ["" "one" "two" "three" "four" "five" "six" "seven" "eight" "nine"])
(def small-number-prefixes ^:private ["n" "m" "b" "tr" "quadr" "quint" "sext" "sept" "oct" "non"])
(def large-number-unit-prefixes ^:private ["" "un" "duo" "tre" "quattuor" "quinqua" "se" "septe" "octo" "nove"])
(def large-number-ten-prefixes ^:private ["" "dec" "vigint" "trigint" "quadragint" "quinquagint" "sexagint" "septuagint" "octogint" "nonagint"])
(def large-number-hundred-prefixes ^:private ["" "cent" "ducent" "trecent" "quadringent" "quingent" "sescent" "septingent" "octingent" "nongent"])

(defn split-and-reverse-numbers
      ^:private
      [number]
      (->> (str number)
           (#(concat (repeat (- 2 (rem (dec (count %)) 3)) \0) %))
           (reverse)
           (map #(Integer/parseInt %))))

(defn unit-to-ten-modification
      ^:private
      [units tens]
      (let [units-set #{units} tens-set #{tens}]
           (cond (and (some units-set [7 9]) (some tens-set [1 3 4 5 6 7])) "n"
                 (and (some units-set [7 9]) (some tens-set [2 8])) "m"
                 (and (= units 6) (= 8 tens)) "x"
                 (and (some units-set [3 6]) (some tens-set [2 3 4 5 8])) "s"
                 :else "")))

(defn unit-to-hundred-modification
      ^:private
      [units hundreds]
      (let [units-set #{units} hundreds-set #{hundreds}]
           (cond (and (some units-set [7 9]) (some hundreds-set [1 2 3 4 5 6 7])) "n"
                 (and (some units-set [7 9]) (= 8 hundreds)) "m"
                 (and (= units 6) (some hundreds-set [1 8])) "x"
                 (and (some units-set [3 6]) (some hundreds-set [1 3 4 5 8])) "s"
                 :else "")))

(defn ten-to-hundred-modification
      ^:private
      [tens hundreds]
      (if (pos? hundreds)
          (if (> tens 2)
              "a"
              "i")
          ""))

(defn large-number-group-prefix
      ^:private
      [units tens hundreds]
      (if (every? zero? [tens hundreds])
          (nth small-number-prefixes units)
          (str (nth large-number-unit-prefixes units)
               (if (pos? tens)
                   (str (unit-to-ten-modification units tens)
                        (nth large-number-ten-prefixes tens)
                        (ten-to-hundred-modification tens hundreds))
                   (unit-to-hundred-modification units hundreds))
               (nth large-number-hundred-prefixes hundreds))))

(defn big-number-group-name
      ^:private
      [group-number]
      (loop [[ones tens hundreds & remaining] (split-and-reverse-numbers group-number)
             prefix "on"]
            (if (nil? ones)
                prefix
                (recur remaining
                       (str (large-number-group-prefix ones tens hundreds)
                            "illi" prefix)))))

(defn group-name
      ^:private
      [group-number]
      (str (cond (zero? group-number) ""
                 (= group-number 1) "thousand"
                 :else (big-number-group-name (dec group-number)))
           ","))

(defn modified-number-name
      ^:private
      [number suffix]
      (let [number-str (nth number-names number)]
           (condp re-find number-str
                         #"^$"   ""
                         #"ree$" (clojure.string/replace number-str #"ree$" (str "ir" suffix))
                         #"ve$"  (clojure.string/replace number-str #"ve$"  (str "f" suffix))
                         #"t$"   (clojure.string/replace number-str #"t$"   suffix)
                         (str number-str suffix))))

(defn group-string
      ^:private
      [ones tens hundreds]
      (concat (if (pos? hundreds)
                  (list (nth number-names hundreds) "hundred"))
              (if (= tens 1)
                  (list (case ones
                              0 "ten"
                              1 "eleven"
                              2 "twelve"
                              (modified-number-name ones "teen")))
                  (list (str (case tens
                                   2 "twenty"
                                   4 "forty"
                                   (modified-number-name tens "ty"))
                             (if (every? pos? [ones tens]) "-" "")
                             (nth number-names ones))))))

(defn integer-to-string
      [number]
      {:pre [(or (integer? number) (and (string? number) (re-matches #"^\d+$" number)))]}
      (if (or (= number 0) (= number "0"))
          "zero"
          (loop [[ones tens hundreds & remaining] (split-and-reverse-numbers number)
                 group 0
                 number-strings (list)]
                (if (nil? ones)
                    (clojure.string/replace (clojure.string/trim (clojure.string/replace (clojure.string/join " " number-strings) #"\s+" " ")) #",$" "")
                    (recur remaining
                           (inc group)
                           (if (every? zero? [ones tens hundreds])
                               number-strings
                               (concat (group-string ones tens hundreds) (conj number-strings (group-name group)))))))))

; googol is ten duotrigintillion
; googolplex is ten trillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentilliduotrigintatrecentillion