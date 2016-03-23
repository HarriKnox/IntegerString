(ns io.harriknox.NumberName
    (:require [clojure.string]))

(def number-names ^:private ["" "one" "two" "three" "four" "five" "six" "seven" "eight" "nine"])
(def small-number-prefixes ^:private ["n" "m" "b" "tr" "quadr" "quint" "sext" "sept" "oct" "non"])
(def large-number-unit-prefixes ^:private ["" "un" "duo" "tre" "quattuor" "quinqua" "se" "septe" "octo" "nove"])
(def large-number-ten-prefixes ^:private ["" "dec" "vigint" "trigint" "quadragint" "quinquagint" "sexagint" "septuagint" "octogint" "nonagint"])
(def large-number-hundred-prefixes ^:private ["" "cent" "ducent" "trecent" "quadringent" "quingent" "sescent" "septingent" "octingent" "nongent"])

(defn split-and-reverse-numbers
      ^:private
      [number]
      (let [number-str (clojure.string/replace (str number) #"^0+" "")]
           (map #(js/parseInt %)
                (reverse (concat (repeat (- 2 (rem (dec (count number-str)) 3)) \0)
                                 number-str)))))

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

(defn illion-group-name
      [group-number]
      {:pre [(or (and (integer? group-number) (pos? group-number)) (re-matches #"^0*[1-9]\d*$" (str group-number)))]}
      (loop [[ones tens hundreds & remaining] (split-and-reverse-numbers group-number)
             suffix "on"]
            (if (nil? ones)
                suffix
                (recur remaining
                       (str (large-number-group-prefix ones tens hundreds)
                            "illi" suffix)))))

(defn name-of-group
      ^:private
      [group-number]
      (str (cond (zero? group-number) ""
                 (= group-number 1) "thousand"
                 :else (illion-group-name (dec group-number)))
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

(defn number-to-string
      [number]
      {:pre [(or (and (integer? number) (>= number 0)) (re-matches #"^\d+$" (str number)))]}
      (if (or (= number 0) (re-matches #"^0+$" number))
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
                               (concat (group-string ones tens hundreds) (conj number-strings (name-of-group group)))))))))

(defn power-of-10-to-string
      [exponent]
      {:pre [(or (and (integer? exponent) (>= number 0)) (re-matches #"^\d+$" (str number)))]
      (number-to-string (str \1 (clojure.string/join exponent \0))))

; googol is ten duotrigintillion
;
; googolplex is ten trillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentilliduotrigintatrecentillion
;     which is ten trilli + (trestrigintatrecentilli * 32) + duotrigintatrecentillion
;
; Proof:
;
; Known 1: a number that is 1 followed by N zeros can be written as 10^N
; Known 2: given an integer Latin group-prefix-number M, the corresponding big-integer is 10^(3M + 3),
; which is 1 followed by 3M + 3 zeros, as per the first known fact
;     'quadrillion' group-prefix-number is 4, so 10^(3(4) + 3) = 10^(12 + 3 = 15) = 1'000'000'000'000'000
;     'quad' means 4                                                                q tri bil mil tho
;
;     'octovigintacentillion' group-prefix-number is 128, so 10^(3(128) - 3) = 10^381
;      
;
; Given an integer that is 1 followed by N zeros, the corresponding group-prefix-number is M = floor[(N - 3)/3]
;                 10^N = 10^(3M + 3) Truth statement
;                    N = 3M + 3      Logarithmize
;                N - 3 = 3M          Subtract 3
;     floor[(N - 3)/3] = M           Floor divide 3
;
;     If 3 divides N, M = floor[(N - 3)/3] = (N - 3)/3
;
;     If 3 does not divide N, M = floor[(N - 3)/3] will equal either ((N - 1) - 3)/3 or ((N - 2) - 3)/3
;     since 3 divides either N, N - 1, or N - 2.
;
; Given an integer R to have a number that is 1 followed by N = 10^R zeros it can be written as 10^(10^R)
;     a 'googol' is 1 followed by 100 zeros, 10^100 = 10^(10^2), so R = 2
;
;     The corresponding group-prefix-number for 10^(10^R) is M = floor[(10^R - 3)/3]
;         3 does not divide 10^R for any R, but 10^R and 1 are congruent modulo 3, so 3 divides 10^R - 1,
;         therefore N' = N - 1 = 10^R - 1, and M' = (N' - 3)/3 = ((10^R - 1) - 3)/3
;
;         N  = 10^R     = 10...00 (0 repeated R times)
;         N' = 10^R - 1 =  9...99 (9 repeated R times)
;
;     Calculating M':
;         M' = (9...99 - 3)/3 (9 repeated R times)
;            = 9...96/3       (9 repeated R - 1 times, then a 6)
;            = 3...32         (3 repeated R - 1 times, then a 2)
;
;     The group-prefix-number for 10^(10^R - 1) is 3...32 (3 repeated R - 1 times)
;
;     N is ten times N', so the written form is "ten" then the Latin name for the group-prefix-number for 10^(10^R - 1):
;         N = 10^(10^R) = 10^(10^R - 1 + 1) = 10^(10^R - 1) * 10^1 = N' * 10
;
; A googol is 10^100
; A googolplex is 10^(googol) = 10^(10^100), so R = 100
;     M' = 3...32 (3 repeated 99 times, then a 2)
;        = 3'333'333'333'333'333'333'333'333'333'333'333'333'333'333'333'333'333'333'333'333'333'333'333'333'333'333'333'333'333'333'333'333'332
;        = 3 (333 repeated 32 times) 332
;        -> trilli (trestrigintatrecentilli * 32) duotrigintatrecentillion
;
;     A googolplex is ten trilli (trestrigintatrecentilli * 32) duotrigintatrecentillion which is
;     ten trillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentillitrestrigintatrecentilliduotrigintatrecentillion
; QED
