package com.example.tensorflowliteapp

class Translator {

    val recognizableObjects = mapOf(
        "всичк" to "all",
        "пожарни кран" to  "fire hydrant",
        "знака стоп" to  "stop sign",
        "бейзболни бухалк" to  "baseball bat",
        "бейзболни ръкавиц" to  "baseball glove",
        "дъски за сърф" to  "surfboard",
        "чаши за вино" to  "wine glass",
        "плюшени мече" to  "teddy bear",
        "четки за зъби" to  "toothbrush",

        "човек" to  "person",
        "хора" to  "person",

        "колел" to  "bicycle",
        "велосипед" to  "bicycle",

        "автомобил" to  "car",
        "кол" to  "car",

        "мотоциклет" to  "motorcycle",
        "мотор" to  "motorcycle",

        "самолет" to  "airplane",
        "автобус" to  "bus",
        "влак" to  "train",
        "камион" to  "truck",

        "лодк" to  "boat",
        "кораб" to  "boat",

        "светофар" to  "traffic light",

        "пожарен хидрант" to  "fire hydrant",
        "пожарен кран" to  "fire hydrant",

        "знак за спиране" to  "stop sign",
        "знак стоп" to  "stop sign",

        "паркинг метър" to  "parking meter",
        "пейк" to  "bench",
        "птиц" to  "bird",
        "котк" to  "cat",
        "куче" to  "dog",
        "кон" to  "horse",
        "овц" to  "sheep",
        "крав" to  "cow",
        "слон" to  "elephant",
        "мечк" to  "bear",
        "зебр" to  "zebra",
        "жираф" to  "giraffe",

        "раниц" to  "backpack",
        "чант" to  "backpack",

        "чадър" to  "umbrella",
        "торб" to  "handbag",

        "вратовръзк" to  "tie",
        "куфар" to  "suitcase",
        "фризби" to  "frisbee",
        "ски" to  "skis",
        "сноуборд" to  "snowboard",

        "спортна топк" to  "sports ball",
        "топк" to "sports ball",

        "хвърчил" to  "kite",
        "бейзболна бухалка" to  "baseball bat",
        "бейзболна ръкавица" to  "baseball glove",
        "скейтборд" to  "skateboard",

        "дъска за сърф" to  "surfboard",
        "сърф дъск" to  "surfboard",

        "ракета за тенис" to  "tennis racket",
        "тенис ракет" to  "tennis racket",

        "бутилк" to  "bottle",
        "шише" to  "bottle",

        "чаша за вино" to  "wine glass",
        "чаш" to  "cup",
        "вилиц" to  "fork",
        "нож" to  "knife",
        "лъжиц" to  "spoon",
        "куп" to  "bowl",
        "банан" to  "banana",
        "ябълк" to  "apple",
        "сандвич" to  "sandwich",
        "портокал" to  "orange",
        "броколи" to  "broccoli",
        "морков" to  "carrot",
        "хот-дог" to  "hot dog",
        "хот дог" to  "hot dog",
        "пиц" to  "pizza",

        "поничк" to  "donut",
        "донът" to  "donut",

        "торт" to  "cake",
        "стол" to  "chair",
        "диван" to  "couch",
        "растени" to  "potted plant",

        "легл" to  "bed",
        "креват" to  "bed",

        "мас" to  "dining table",
        "тоалетн" to  "toilet",

        "телевизор" to  "tv",
        "монитор" to  "tv",

        "лаптоп" to  "laptop",
        "мишк" to  "mouse",

        "дистанцион" to  "remote",
        "клавиатур" to  "keyboard",

        "телефон" to  "cell phone",

        "микровълнов" to  "microwave",
        "фурн" to  "oven",
        "тостер" to  "toaster",
        "мивк" to  "sink",
        "хладилник" to  "refrigerator",
        "книг" to  "book",
        "часовник" to  "clock",
        "ваз" to  "vase",
        "ножиц" to  "scissors",
        "плюшено мече" to  "teddy bear",
        "сешоар" to  "hair drier",
        "четка за зъби" to  "toothbrush",

        "all" to "all",
        "person" to "person",
        "human" to "person",

        "bicycle" to  "bicycle",
        "bike" to  "bicycle",

        "car" to  "car",
        "automobile" to  "car",
        "vehicle" to  "car",

        "motorcycle" to  "motorcycle",
        "motorbike" to  "motorcycle",

        "plane" to  "airplane",
        "aircraft" to  "airplane",

        "bus" to  "bus",
        "train" to  "train",
        "locomotive" to  "train",

        "truck" to  "truck",
        "lorry" to  "truck",

        "boat" to  "boat",
        "ship" to  "boat",

        "traffic light" to  "traffic light",
        "hydrant" to  "fire hydrant",
        "stop sign" to  "stop sign",
        "parking meter" to  "parking meter",
        "bench" to  "bench",
        "bird" to  "bird",
        "cat" to  "cat",
        "dog" to  "dog",
        "horse" to  "horse",

        "sheep" to  "sheep",
        "lamb" to  "sheep",

        "cow" to  "cow",
        "elephant" to  "elephant",
        "bear" to  "bear",
        "zebra" to  "zebra",
        "giraffe" to  "giraffe",
        "backpack" to  "backpack",
        "umbrella" to  "umbrella",
        "handbag" to  "handbag",
        "tie" to  "tie",

        "suitcase" to  "suitcase",
        "luggage" to  "suitcase",

        "frisbee" to  "frisbee",
        "flying disc" to  "frisbee",

        "skis" to  "skis",
        "snowboard" to  "snowboard",
        "ball" to  "sports ball",
        "kite" to  "kite",
        "baseball bat" to  "baseball bat",
        "baseball glove" to  "baseball glove",
        "skateboard" to  "skateboard",
        "surfboard" to  "surfboard",
        "racket" to "tennis racket",
        "bottle" to  "bottle",
        "wine glass" to  "wine glass",

        "cup" to  "cup",
        "glass" to  "cup",

        "fork" to  "fork",
        "knife" to  "knife",
        "spoon" to  "spoon",
        "bowl" to  "bowl",
        "banana" to  "banana",
        "apple" to  "apple",
        "sandwich" to  "sandwich",
        "orange" to  "orange",
        "broccoli" to  "broccoli",
        "carrot" to  "carrot",
        "hot dog" to  "hot dog",
        "pizza" to  "pizza",

        "donut" to  "donut",
        "doughnut" to  "donut",

        "cake" to  "cake",

        "chair" to  "chair",
        "seat" to  "chair",

        "couch" to  "couch",
        "plant" to  "potted plant",
        "bed" to  "bed",
        "dining table" to  "dining table",
        "toilet" to  "toilet",

        "tv" to  "tv",
        "television" to  "tv",
        "monitor" to  "tv",

        "laptop" to  "laptop",
        "mouse" to  "mouse",
        "remote" to  "remote",
        "keyboard" to  "keyboard",
        "cell phone" to  "cell phone",
        "microwave" to  "microwave",
        "oven" to  "oven",
        "toaster" to  "toaster",
        "sink" to  "sink",

        "refrigerator" to  "refrigerator",
        "fridge" to  "refrigerator",

        "book" to  "book",
        "clock" to  "clock",
        "vase" to  "vase",

        "scissors" to  "scissors",
        "shears" to  "scissors",

        "teddy bear" to  "teddy bear",

        "hair drier" to  "hair drier",
        "blow dryer" to  "hair drier",
        "hair blower" to  "hair drier",

        "toothbrush" to  "toothbrush"
    )

    val translateToBG = mapOf(
        "person" to "човек",
        "bicycle" to "колело",
        "car" to "кола",
        "motorcycle" to "мотоциклет",
        "airplane" to "самолет",
        "bus" to "автобус",
        "train" to "влак",
        "truck" to "камион",
        "boat" to "лодка",
        "traffic light" to "светофар",
        "fire hydrant" to "пожарен кран",
        "stop sign" to "знак стоп",
        "parking meter" to "паркинг автомат",
        "bench" to "пейка",
        "bird" to "птица",
        "cat" to "котка",
        "dog" to "куче",
        "horse" to "кон",
        "sheep" to "офца",
        "cow" to "крава",
        "elephant" to "слон",
        "bear" to "мечка",
        "zebra" to "зебра",
        "giraffe" to "жираф",
        "backpack" to "раница",
        "umbrella" to "чадър",
        "handbag" to "дамска чанта",
        "tie" to "вратовръзка",
        "suitcase" to "куфар",
        "frisbee" to "фризби",
        "skis" to "ски",
        "snowboard" to "сноуборд",
        "sports ball" to "спортна топка",
        "kite" to "хвърчило",
        "baseball bat" to "бейзболна бухалка",
        "baseball glove" to "бейзболна ръкавица",
        "skateboard" to "скейтборд",
        "surfboard" to "дъска за сърф",
        "tennis racket" to "тенис ракета",
        "bottle" to "бутилка",
        "wine glass" to "чаша за вино",
        "cup" to "чаша",
        "fork" to "вилица",
        "knife" to "нож",
        "spoon" to "лъжица",
        "bowl" to "купа",
        "banana" to "банан",
        "apple" to "ябълка",
        "sandwich" to "сандвич",
        "orange" to "портокал",
        "broccoli" to "броколи",
        "carrot" to "морков",
        "hot dog" to "хот дог",
        "pizza" to "пица",
        "donut" to "донът",
        "cake" to "торта",
        "chair" to "стол",
        "couch" to "диван",
        "potted plant" to "растение в саксия",
        "bed" to "легло",
        "dining table" to "маса",
        "toilet" to "тоалетна",
        "tv" to "телевизор",
        "laptop" to "лаптоп",
        "mouse" to "мишка",
        "remote" to "дистанционно",
        "keyboard" to "клавиатура",
        "cell phone" to "телефон",
        "microwave" to "микроволнова",
        "oven" to "фурна",
        "toaster" to "тостер",
        "sink" to "мивка",
        "refrigerator" to "хладилник",
        "book" to "книга",
        "clock" to "часовник",
        "vase" to "ваза",
        "scissors" to "ножици",
        "teddy bear" to "плюшено мече",
        "hair drier" to "сешоар",
        "toothbrush" to "четка за зъби",
        "This object can be found." to "Този предмет може да се открие.",
        "Starting search" to "Почва търсене",
        "Mode is stopped. What is your next command?" to "Спряхте режима. Какво искате да направя за вас?",
        "Starting navigation" to "Пускане на навигация",
        "This object can NOT be found. Try with something else" to "Този предмет може да бъде разпознат. Опитайте с нещо друго.",
    )

    val plural = mapOf(
        "person" to "човека",
        "bicycle" to "колела",
        "car" to "коли",
        "motorcycle" to "мотоциклета",
        "airplane" to "самолета",
        "bus" to "автобуса",
        "train" to "влака",
        "truck" to "камиона",
        "boat" to "лодки",
        "traffic light" to "светофара",
        "fire hydrant" to "пожарни крана",
        "stop sign" to "знака стоп",
        "parking meter" to "паркинг автомата",
        "bench" to "пейки",
        "bird" to "птици",
        "cat" to "котки",
        "dog" to "кучета",
        "horse" to "коня",
        "sheep" to "офце",
        "cow" to "крави",
        "elephant" to "слона",
        "bear" to "мечки",
        "zebra" to "зебри",
        "giraffe" to "жирафа",
        "backpack" to "раници",
        "umbrella" to "чадъра",
        "handbag" to "дамска чанти",
        "tie" to "вратовръзки",
        "suitcase" to "куфара",
        "frisbee" to "фризбита",
        "skis" to "ски",
        "snowboard" to "сноуборда",
        "sports ball" to "спортни топки",
        "kite" to "хвърчила",
        "baseball bat" to "бейзболни бухалки",
        "baseball glove" to "бейзболни ръкавици",
        "skateboard" to "скейтборда",
        "surfboard" to "дъски за сърф",
        "tennis racket" to "тенис ракети",
        "bottle" to "бутилки",
        "wine glass" to "чаши за вино",
        "cup" to "чаши",
        "fork" to "вилици",
        "knife" to "ножа",
        "spoon" to "лъжици",
        "bowl" to "купи",
        "banana" to "банана",
        "apple" to "ябълки",
        "sandwich" to "сандвича",
        "orange" to "портокала",
        "broccoli" to "броколи",
        "carrot" to "моркови",
        "hot dog" to "хот дога",
        "pizza" to "пици",
        "donut" to "донъта",
        "cake" to "торти",
        "chair" to "стола",
        "couch" to "дивана",
        "potted plant" to "растения в саксия",
        "bed" to "легла",
        "dining table" to "маси",
        "toilet" to "тоалетни",
        "tv" to "телевизора",
        "laptop" to "лаптопа",
        "mouse" to "мишки",
        "remote" to "дистанционни",
        "keyboard" to "клавиатури",
        "cell phone" to "телефона",
        "microwave" to "микроволнови",
        "oven" to "фурни",
        "toaster" to "тостера",
        "sink" to "мивки",
        "refrigerator" to "хладилника",
        "book" to "книги",
        "clock" to "часовника",
        "vase" to "вази",
        "scissors" to "ножици",
        "teddy bear" to "плюшени мечета",
        "hair drier" to "сешоара",
        "toothbrush" to "четки за зъби",
        "find" to "намери",
    )

    fun number(br : Int, word : String, lang:String) : String? {
        var word = word
        if (lang.equals("bg")) {
            word = translateToBG[word].toString()
            if (br > 2) {
                return br.toString()
            } else if (br == 1) {
                if (word?.let { rod(it) } == 1) {
                    return "Един"
                } else if (word?.let { rod(it) } == 2) {
                    return "Една"
                } else {
                    return "Едно"
                }
            } else if (br == 2) {
                if (word?.let { rod(it) } == 1) {
                    return "Два"
                } else if (word?.let { rod(it) } == 2) {
                    return "Две"
                } else {
                    return "Две"
                }
            }
        }
        return br.toString()
    }

    fun rod(word : String): Int {

        val words = listOf(word.split(" "))
        val lastChar = words[0][words[0].size-1]
        if(lastChar == "a" || lastChar == "я"){
            return 2
        }else if(lastChar == "o" || lastChar == "e"){
            return 3
        }else{
            return 1
        }
    }

    fun translate(word: String, lang: String): String? {
        if (lang.equals("bg") && translateToBG.containsKey(word)){
            return translateToBG[word]
        }
        return word
    }

    fun recognizeObject(word: String): String? {
        if (recognizableObjects.containsKey(word)){
            return recognizableObjects[word]
        }
        return null
    }

}