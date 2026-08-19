package com.example.data

import com.example.model.Drug
import com.example.model.DrugCategory
import com.example.model.Formulation
import com.example.model.FormulationType
import com.example.model.IndianBrand
import com.example.model.IndicationRegimen

object DrugDatabase {

  val drugs: List<Drug> = listOf(
    // 1. AMOXICILLIN
    Drug(
      id = "amoxicillin",
      name = "Amoxicillin",
      genericName = "Amoxicillin Trihydrate",
      category = DrugCategory.ANTIBIOTIC,
      subtitle = "Broad-spectrum Beta-Lactam Antibiotic",
      description = "Moderate-spectrum bactericidal penicillin used for acute otitis media, strep pharyngitis, sinusitis, community-acquired pneumonia, and urinary tract infections.",
      standardRegimenSummary = "20–40 mg/kg/day divided every 8 hours (q8h) or 25–45 mg/kg/day divided every 12 hours (q12h). Max 500 mg/single dose or 1500 mg/day for standard infections.",
      adultDoseSummary = "250–500 mg PO every 8 hours or 500–875 mg PO every 12 hours.",
      minAgeMonths = 1,
      defaultRegimen = IndicationRegimen(
        id = "amox_std",
        name = "Standard Infection (Mild-Moderate)",
        description = "20–40 mg/kg/day divided every 8 hours (q8h) for RTI, pharyngitis, skin infections.",
        defaultMgPerKgPerDay = 30.0,
        frequencyTimesPerDay = 3,
        frequencyDescription = "Every 8 hours (3 times daily)",
        maxDailyDoseMg = 1500.0,
        maxSingleDoseMg = 500.0,
        standardDurationDays = 7
      ),
      alternativeRegimens = listOf(
        IndicationRegimen(
          id = "amox_aom_high",
          name = "Acute Otitis Media / Pneumonia (High Dose)",
          description = "80–90 mg/kg/day divided every 12 hours (q12h) to eradicate resistant Streptococcus pneumoniae.",
          defaultMgPerKgPerDay = 90.0,
          frequencyTimesPerDay = 2,
          frequencyDescription = "Every 12 hours (2 times daily with meals)",
          maxDailyDoseMg = 2000.0,
          maxSingleDoseMg = 1000.0,
          standardDurationDays = 10
        ),
        IndicationRegimen(
          id = "amox_strep",
          name = "Streptococcal Pharyngitis (Once/Twice Daily)",
          description = "50 mg/kg/day (once daily or divided q12h) for 10 days. Max 1000 mg/day.",
          defaultMgPerKgPerDay = 50.0,
          frequencyTimesPerDay = 1,
          frequencyDescription = "Once daily (or 25 mg/kg every 12 hours)",
          maxDailyDoseMg = 1000.0,
          maxSingleDoseMg = 1000.0,
          standardDurationDays = 10
        )
      ),
      formulations = listOf(
        Formulation("amox_125", "Oral Suspension 125 mg / 5 mL", FormulationType.ORAL_SUSPENSION, 125.0, 5.0, "125mg/5mL", listOf(60.0, 100.0)),
        Formulation("amox_250", "Oral Suspension 250 mg / 5 mL", FormulationType.ORAL_SUSPENSION, 250.0, 5.0, "250mg/5mL", listOf(60.0, 100.0)),
        Formulation("amox_400", "Oral Suspension 400 mg / 5 mL (Extra Strength)", FormulationType.ORAL_SUSPENSION, 400.0, 5.0, "400mg/5mL", listOf(75.0, 100.0)),
        Formulation("amox_500_tab", "Capsule / Tablet 500 mg", FormulationType.TABLET, 500.0, 1.0, "500mg Tablet")
      ),
      indications = listOf("Acute Otitis Media", "Streptococcal Pharyngitis", "Community Acquired Pneumonia", "Sinusitis", "Skin & Soft Tissue Infections", "UTI"),
      contraindications = listOf("Known severe hypersensitivity/anaphylaxis to penicillins or beta-lactams", "Infectious mononucleosis (high incidence of erythematous maculopapular rash)"),
      warnings = listOf("Reduce dosage in severe renal impairment (GFR < 30 mL/min)", "Superinfection with C. difficile diarrhea may occur", "Complete full prescribed duration"),
      sideEffects = listOf("Diarrhea / Loose stools", "Nausea", "Maculopapular skin rash", "Vomiting"),
      administrationAdvice = "Can be administered without regard to meals. Liquid suspension can be added directly to milk, juice, or water immediately before ingestion.",
      reconstitutionStorage = "Reconstituted oral suspension is stable for 14 days. Refrigeration is preferred for taste preservation, but not mandatory. Shake well before each dose.",
      renalAdjustmentNote = "GFR 10–30 mL/min: 250–500 mg q12h; GFR < 10 mL/min: 250–500 mg q24h.",
      references = listOf(
        "American Academy of Pediatrics (AAP) Clinical Practice Guideline: The Diagnosis and Management of Acute Otitis Media. Pediatrics. 2013;131(3):e964-e999.",
        "British National Formulary for Children (BNF-C) 2025–2026: Amoxicillin Dosing in Pediatric Infections.",
        "Infectious Diseases Society of America (IDSA) Clinical Practice Guideline for the Diagnosis and Management of Group A Streptococcal Pharyngitis. Clin Infect Dis. 2012;55(10):1279-1282.",
        "Nelson Textbook of Pediatrics, 21st Edition, Chapter 738: Penicillins and Cephalosporins."
      ),
      indianBrands = listOf(
        IndianBrand("Novamox", "Cipla Ltd", "Paediatric Drops 100mg/mL, Dry Syrup 125mg/5mL, 250mg/5mL", "30 mL / 60 mL bottle"),
        IndianBrand("Mox", "Sun Pharma", "Redimix Susp 125mg/5mL, 250mg/5mL, Drops 100mg/mL", "30 mL / 60 mL bottle"),
        IndianBrand("Almox", "Alkem Laboratories", "Dry Syrup 125mg/5mL, 250mg/5mL", "60 mL bottle"),
        IndianBrand("Cipmox", "Cipla Ltd", "Suspension 125mg/5mL, 250mg/5mL, Cap 250/500mg", "60 mL bottle"),
        IndianBrand("Wymox", "Pfizer India", "Dry Syrup 125mg/5mL, 250mg/5mL", "60 mL bottle")
      )
    ),

    // 2. PARACETAMOL / ACETAMINOPHEN
    Drug(
      id = "paracetamol",
      name = "Paracetamol (Acetaminophen)",
      genericName = "Acetaminophen",
      category = DrugCategory.ANTIPYRETIC_ANALGESIC,
      subtitle = "First-line Pediatric Antipyretic & Analgesic",
      description = "Central acting analgesic and antipyretic agent indicated for temporary relief of mild-to-moderate pain and fever reduction.",
      standardRegimenSummary = "10–15 mg/kg/dose every 4–6 hours as needed. Maximum 4–5 doses (or 60–75 mg/kg/day or max 4000 mg/day).",
      adultDoseSummary = "500–1000 mg PO every 4–6 hours as needed. Maximum 4000 mg/day (or 3000 mg/day in chronic use).",
      minAgeMonths = 1,
      defaultRegimen = IndicationRegimen(
        id = "pcm_std",
        name = "Standard Antipyretic / Analgesic",
        description = "15 mg/kg/dose given every 4–6 hours as needed for fever or pain.",
        defaultMgPerKgPerDay = 60.0,
        defaultMgPerKgPerDose = 15.0,
        frequencyTimesPerDay = 4,
        frequencyDescription = "Every 4 to 6 hours as needed (Max 4-5 times in 24 hours)",
        maxDailyDoseMg = 4000.0,
        maxSingleDoseMg = 1000.0,
        standardDurationDays = 3
      ),
      alternativeRegimens = listOf(
        IndicationRegimen(
          id = "pcm_mild",
          name = "Low-Dose / Mild Discomfort",
          description = "10 mg/kg/dose given every 6 hours as needed.",
          defaultMgPerKgPerDay = 40.0,
          defaultMgPerKgPerDose = 10.0,
          frequencyTimesPerDay = 4,
          frequencyDescription = "Every 6 hours as needed",
          maxDailyDoseMg = 3000.0,
          maxSingleDoseMg = 650.0,
          standardDurationDays = 3
        )
      ),
      formulations = listOf(
        Formulation("pcm_120", "Syrup 120 mg / 5 mL (Infant/Pediatric)", FormulationType.SYRUP, 120.0, 5.0, "120mg/5mL", listOf(60.0, 100.0)),
        Formulation("pcm_160", "Children's Suspension 160 mg / 5 mL", FormulationType.ORAL_SUSPENSION, 160.0, 5.0, "160mg/5mL", listOf(118.0)),
        Formulation("pcm_250", "Forte Syrup 250 mg / 5 mL", FormulationType.SYRUP, 250.0, 5.0, "250mg/5mL", listOf(60.0, 100.0)),
        Formulation("pcm_drops", "Infant Drops 100 mg / 1 mL (Concentrated)", FormulationType.DROPS, 100.0, 1.0, "100mg/1mL", listOf(15.0, 30.0)),
        Formulation("pcm_500_tab", "Tablet 500 mg", FormulationType.TABLET, 500.0, 1.0, "500mg Tablet"),
        Formulation("pcm_650_tab", "Tablet 650 mg", FormulationType.TABLET, 650.0, 1.0, "650mg Tablet")
      ),
      indications = listOf("Fever reduction", "Mild to moderate pain", "Post-vaccination fever", "Headache", "Teething pain", "Sore throat"),
      contraindications = listOf("Severe active hepatic impairment or acute liver failure", "Severe hypersensitivity to acetaminophen"),
      warnings = listOf("Hepatotoxicity risk when combined with other acetaminophen products", "Never exceed 75 mg/kg/day in children or 4000 mg/day in adults", "Use calibrated oral syringe for infant drops"),
      sideEffects = listOf("Generally very well tolerated at therapeutic doses", "Rare allergic rash", "Elevated AST/ALT with overdose"),
      administrationAdvice = "May be taken with or without food. Do not give more than 4 doses in 24 hours unless directed by a pediatrician.",
      reconstitutionStorage = "Store at room temperature 20°C to 25°C. Protect from freezing and excessive heat.",
      renalAdjustmentNote = "Severe renal impairment (GFR < 10 mL/min): extend dosing interval to every 8 hours.",
      references = listOf(
        "American Academy of Pediatrics (AAP) Clinical Report: Fever and Antipyretic Use in Children. Pediatrics. 2011;127(3):580-587.",
        "World Health Organization (WHO) Model List of Essential Medicines for Children (EMLc) 9th List (2023): Paracetamol.",
        "British National Formulary for Children (BNF-C) 2025–2026: Paracetamol / Acetaminophen Pediatric Regimens.",
        "Nelson Textbook of Pediatrics, 21st Edition: Pediatric Pain Management and Antipyresis."
      ),
      indianBrands = listOf(
        IndianBrand("Calpol", "GlaxoSmithKline (GSK)", "Paediatric Drops 100mg/mL, Pead Susp 120mg/5mL, Calpol 250 Plus Susp", "15 mL / 60 mL / 120 mL"),
        IndianBrand("Dolo", "Micro Labs Ltd", "Infant Drops 100mg/mL, Susp 120mg/5mL, Dolo-250 Susp 250mg/5mL", "15 mL / 60 mL"),
        IndianBrand("Crocin", "Haleon / GSK", "Baby Drops 100mg/mL, Syrup 120mg/5mL, Crocin DS 240mg/5mL", "15 mL / 60 mL"),
        IndianBrand("P-120 / P-250", "Apex Laboratories", "Drops 100mg/mL, P-120 Susp 120mg/5mL, P-250 Susp 250mg/5mL", "15 mL / 60 mL"),
        IndianBrand("Sumo L", "Alkem Laboratories", "Drops 100mg/mL, DS Susp 250mg/5mL", "15 mL / 60 mL"),
        IndianBrand("T-98", "Mankind Pharma", "Paediatric Drops 100mg/mL, Susp 120mg/5mL, 250mg/5mL", "15 mL / 60 mL")
      )
    ),

    // 3. IBUPROFEN
    Drug(
      id = "ibuprofen",
      name = "Ibuprofen",
      genericName = "Ibuprofen",
      category = DrugCategory.ANTIPYRETIC_ANALGESIC,
      subtitle = "NSAID Antipyretic, Analgesic & Anti-inflammatory",
      description = "Nonsteroidal anti-inflammatory drug (NSAID) for inflammatory pain, fever, juvenile arthritis, and musculoskeletal aches. Highly effective for high fever.",
      standardRegimenSummary = "5–10 mg/kg/dose every 6–8 hours as needed. Maximum 40 mg/kg/day (not to exceed 2400 mg/day).",
      adultDoseSummary = "200–400 mg PO every 4–6 hours or 600–800 mg PO every 8 hours (Max 2400 mg/day).",
      minAgeMonths = 3,
      defaultRegimen = IndicationRegimen(
        id = "ibu_std",
        name = "Standard Antipyretic & Analgesic (High Fever / Pain)",
        description = "10 mg/kg/dose given every 6–8 hours with meals or milk.",
        defaultMgPerKgPerDay = 30.0,
        defaultMgPerKgPerDose = 10.0,
        frequencyTimesPerDay = 3,
        frequencyDescription = "Every 6 to 8 hours with food (Max 3-4 doses in 24h)",
        maxDailyDoseMg = 2400.0,
        maxSingleDoseMg = 600.0,
        minAgeMonths = 3,
        standardDurationDays = 3
      ),
      alternativeRegimens = listOf(
        IndicationRegimen(
          id = "ibu_low",
          name = "Mild Fever (<39°C / 102.5°F)",
          description = "5 mg/kg/dose given every 6–8 hours.",
          defaultMgPerKgPerDay = 20.0,
          defaultMgPerKgPerDose = 5.0,
          frequencyTimesPerDay = 3,
          frequencyDescription = "Every 6 to 8 hours with food",
          maxDailyDoseMg = 1200.0,
          maxSingleDoseMg = 400.0,
          minAgeMonths = 3,
          standardDurationDays = 3
        ),
        IndicationRegimen(
          id = "ibu_jia",
          name = "Juvenile Idiopathic Arthritis",
          description = "30–40 mg/kg/day divided into 3–4 doses. Max 2400 mg/day.",
          defaultMgPerKgPerDay = 35.0,
          frequencyTimesPerDay = 3,
          frequencyDescription = "Every 8 hours with meals",
          maxDailyDoseMg = 2400.0,
          maxSingleDoseMg = 800.0,
          minAgeMonths = 6,
          standardDurationDays = 14
        )
      ),
      formulations = listOf(
        Formulation("ibu_100", "Oral Suspension 100 mg / 5 mL", FormulationType.ORAL_SUSPENSION, 100.0, 5.0, "100mg/5mL", listOf(60.0, 100.0, 150.0)),
        Formulation("ibu_200_susp", "Forte Suspension 200 mg / 5 mL", FormulationType.ORAL_SUSPENSION, 200.0, 5.0, "200mg/5mL", listOf(100.0)),
        Formulation("ibu_drops", "Infant Drops 50 mg / 1.25 mL (40 mg/mL)", FormulationType.DROPS, 40.0, 1.0, "40mg/1mL", listOf(15.0, 30.0)),
        Formulation("ibu_200_tab", "Tablet 200 mg", FormulationType.TABLET, 200.0, 1.0, "200mg Tablet"),
        Formulation("ibu_400_tab", "Tablet 400 mg", FormulationType.TABLET, 400.0, 1.0, "400mg Tablet")
      ),
      indications = listOf("Fever unresponsive to paracetamol", "Inflammatory pain", "Otitis media pain", "Musculoskeletal pain", "Dental pain", "Juvenile idiopathic arthritis"),
      contraindications = listOf("Infants younger than 3 months or < 5 kg", "Active GI bleeding or ulceration", "Severe dehydration / renal insufficiency", "Aspirin-induced asthma / bronchospasm"),
      warnings = listOf("Ensure patient is adequately hydrated to prevent NSAID nephrotoxicity", "Administer with food or milk to minimize GI irritation", "Avoid in chickenpox (varicella) due to risk of invasive skin infections"),
      sideEffects = listOf("Gastric upset / dyspepsia", "Nausea", "Abdominal discomfort", "Mild transient elevation of creatinine"),
      administrationAdvice = "Always take with food, milk, or immediately after a meal to protect the stomach lining.",
      reconstitutionStorage = "Store at room temperature. Shake bottle thoroughly before each administration.",
      renalAdjustmentNote = "Avoid in acute kidney injury or severe renal impairment (GFR < 30 mL/min).",
      references = listOf(
        "American Academy of Pediatrics (AAP): Management of Pediatric Fever and Pain. Pediatrics. 2011;127(3):580-587.",
        "FDA Drug Safety Communication: Nonsteroidal Anti-Inflammatory Drugs in Pediatrics and Pregnancy Warnings.",
        "British National Formulary for Children (BNF-C) 2025–2026: Ibuprofen Pediatric Dosing Guidelines."
      ),
      indianBrands = listOf(
        IndianBrand("Ibugesic", "Cipla Ltd", "Oral Suspension 100mg/5mL, Plus Susp (with Paracetamol)", "60 mL / 100 mL bottle"),
        IndianBrand("Brufen", "Abbott India", "Junior Syrup 100mg/5mL, Brufen 200/400 Tablets", "60 mL / 100 mL bottle"),
        IndianBrand("Combiflam", "Sanofi India", "Suspension (Ibuprofen 100mg + Paracetamol 162.5mg / 5mL)", "60 mL / 100 mL bottle"),
        IndianBrand("Ibukem", "Alkem Laboratories", "Oral Suspension 100mg/5mL", "60 mL bottle")
      )
    ),

    // 4. AZITHROMYCIN
    Drug(
      id = "azithromycin",
      name = "Azithromycin",
      genericName = "Azithromycin Dihydrate",
      category = DrugCategory.ANTIBIOTIC,
      subtitle = "Macrolide Antibiotic (Atypical & Respiratory)",
      description = "Extended-spectrum azalide macrolide for community-acquired pneumonia, acute bacterial sinusitis, streptococcal pharyngitis in penicillin-allergic patients, and pertussis.",
      standardRegimenSummary = "10 mg/kg once daily on Day 1, then 5 mg/kg once daily on Days 2–5. Max 500 mg Day 1, 250 mg Days 2–5.",
      adultDoseSummary = "500 mg PO on Day 1, followed by 250 mg PO once daily on Days 2–5.",
      minAgeMonths = 6,
      defaultRegimen = IndicationRegimen(
        id = "azith_5day",
        name = "Standard 5-Day Respiratory Protocol",
        description = "10 mg/kg on Day 1 (max 500mg), followed by 5 mg/kg once daily for Days 2–5 (max 250mg).",
        defaultMgPerKgPerDay = 10.0,
        frequencyTimesPerDay = 1,
        frequencyDescription = "Once daily (Day 1 full dose, Days 2-5 half dose)",
        maxDailyDoseMg = 500.0,
        maxSingleDoseMg = 500.0,
        standardDurationDays = 5
      ),
      alternativeRegimens = listOf(
        IndicationRegimen(
          id = "azith_strep_3day",
          name = "Strep Pharyngitis / 3-Day Protocol",
          description = "10 mg/kg once daily for 3 consecutive days (Max 500 mg/day). Total dose 30 mg/kg.",
          defaultMgPerKgPerDay = 10.0,
          frequencyTimesPerDay = 1,
          frequencyDescription = "Once daily for 3 days",
          maxDailyDoseMg = 500.0,
          maxSingleDoseMg = 500.0,
          standardDurationDays = 3
        )
      ),
      formulations = listOf(
        Formulation("azith_100", "Oral Suspension 100 mg / 5 mL", FormulationType.ORAL_SUSPENSION, 100.0, 5.0, "100mg/5mL", listOf(15.0, 30.0)),
        Formulation("azith_200", "Oral Suspension 200 mg / 5 mL", FormulationType.ORAL_SUSPENSION, 200.0, 5.0, "200mg/5mL", listOf(15.0, 30.0)),
        Formulation("azith_250_tab", "Tablet 250 mg", FormulationType.TABLET, 250.0, 1.0, "250mg Tablet"),
        Formulation("azith_500_tab", "Tablet 500 mg", FormulationType.TABLET, 500.0, 1.0, "500mg Tablet")
      ),
      indications = listOf("Community-Acquired Pneumonia (Mycoplasma/Chlamydia)", "Acute Bacterial Sinusitis", "Penicillin-allergic Pharyngitis", "Pertussis Treatment/Prophylaxis"),
      contraindications = listOf("Hypersensitivity to azithromycin, erythromycin, or any macrolide", "History of cholestatic jaundice or hepatic dysfunction with prior macrolide use"),
      warnings = listOf("May cause QT prolongation and ventricular arrhythmias in predisposed patients", "Infantile hypertrophic pyloric stenosis reported in neonates < 42 days"),
      sideEffects = listOf("Diarrhea / Abdominal cramps", "Nausea", "Vomiting", "Temporary alteration in taste"),
      administrationAdvice = "Can be taken with or without food. Taking with food reduces GI discomfort. Avoid concurrent antacids containing aluminum or magnesium.",
      reconstitutionStorage = "Store dry powder at room temperature. After reconstitution, suspension is stable for 10 days at 5°C to 30°C. Do not refrigerate.",
      renalAdjustmentNote = "No dosage adjustment needed for mild-moderate renal impairment; caution if GFR < 10 mL/min.",
      references = listOf(
        "Pediatric Infectious Diseases Society and the Infectious Diseases Society of America (PIDS/IDSA): Clinical Practice Guideline on Community-Acquired Pneumonia in Infants and Children. Clin Infect Dis. 2011;53(7):e25-e76.",
        "CDC Guidelines for the Prevention and Control of Pertussis: Azithromycin Recommendations.",
        "Nelson Textbook of Pediatrics, 21st Edition: Macrolides and Atypical Pneumonias."
      ),
      indianBrands = listOf(
        IndianBrand("Azithral", "Alembic Pharmaceuticals", "Liquid 100mg/5mL, 200mg/5mL, XL 200 Ready Susp", "15 mL / 30 mL bottle"),
        IndianBrand("Azee", "Cipla Ltd", "Dry Syrup 100mg/5mL, 200mg/5mL, Redidose 200", "15 mL / 30 mL bottle"),
        IndianBrand("Zady", "Mankind Pharma", "Readymix Susp 100mg/5mL, 200mg/5mL", "15 mL / 30 mL bottle"),
        IndianBrand("ATM", "Indoco Remedies", "Oral Suspension 100mg/5mL, 200mg/5mL", "15 mL / 30 mL bottle"),
        IndianBrand("Aziwok", "Wockhardt Ltd", "Liquid 100mg/5mL, 200mg/5mL", "15 mL / 30 mL bottle")
      )
    ),

    // 5. AUGMENTIN (CO-AMOXICLAV)
    Drug(
      id = "augmentin",
      name = "Amoxicillin / Clavulanate (Augmentin)",
      genericName = "Amoxicillin + Potassium Clavulanate",
      category = DrugCategory.ANTIBIOTIC,
      subtitle = "Beta-Lactamase Inhibitor Combination",
      description = "Broad-spectrum antibacterial for beta-lactamase producing organisms (H. influenzae, M. catarrhalis, S. aureus, anaerobes), recurrent otitis, bite wounds, and sinusitis.",
      standardRegimenSummary = "25–45 mg/kg/day (amoxicillin component) divided every 12 hours (q12h) or 20–40 mg/kg/day divided every 8 hours (q8h). Max 1750 mg/day.",
      adultDoseSummary = "500/125 mg PO every 8 hours or 875/125 mg PO every 12 hours.",
      minAgeMonths = 2,
      defaultRegimen = IndicationRegimen(
        id = "aug_std_12h",
        name = "Standard Formulation (7:1 ratio - 25-45 mg/kg/day)",
        description = "25–45 mg/kg/day amoxicillin divided every 12 hours for sinusitis, RTI, skin infections.",
        defaultMgPerKgPerDay = 35.0,
        frequencyTimesPerDay = 2,
        frequencyDescription = "Every 12 hours (twice daily at start of meals)",
        maxDailyDoseMg = 1750.0,
        maxSingleDoseMg = 875.0,
        standardDurationDays = 7
      ),
      alternativeRegimens = listOf(
        IndicationRegimen(
          id = "aug_high_aom",
          name = "High-Dose ES (14:1 ratio - 90 mg/kg/day amoxicillin)",
          description = "80–90 mg/kg/day amoxicillin with 6.4 mg/kg/day clavulanate divided q12h for resistant AOM or failed initial therapy.",
          defaultMgPerKgPerDay = 90.0,
          frequencyTimesPerDay = 2,
          frequencyDescription = "Every 12 hours with meals for 10 days",
          maxDailyDoseMg = 2000.0,
          maxSingleDoseMg = 1000.0,
          standardDurationDays = 10
        )
      ),
      formulations = listOf(
        Formulation("aug_156", "Suspension 156.25 mg / 5 mL (125/31.25)", FormulationType.ORAL_SUSPENSION, 125.0, 5.0, "125mg Amox/5mL", listOf(60.0, 100.0)),
        Formulation("aug_312", "Suspension 312.5 mg / 5 mL (250/62.5)", FormulationType.ORAL_SUSPENSION, 250.0, 5.0, "250mg Amox/5mL", listOf(60.0, 100.0)),
        Formulation("aug_228", "Suspension 228.5 mg / 5 mL (200/28.5 - BID)", FormulationType.ORAL_SUSPENSION, 200.0, 5.0, "200mg Amox/5mL", listOf(70.0)),
        Formulation("aug_457", "Suspension 457 mg / 5 mL (400/57 - BID)", FormulationType.ORAL_SUSPENSION, 400.0, 5.0, "400mg Amox/5mL", listOf(70.0, 100.0)),
        Formulation("aug_600_es", "Augmentin ES-600 (600/42.9 mg per 5 mL)", FormulationType.ORAL_SUSPENSION, 600.0, 5.0, "600mg Amox/5mL", listOf(75.0, 125.0)),
        Formulation("aug_625_tab", "Tablet 625 mg (500/125)", FormulationType.TABLET, 500.0, 1.0, "625mg Tablet (500 Amox)")
      ),
      indications = listOf("Recurrent / Resistant Otitis Media", "Acute Bacterial Sinusitis", "Community Acquired Pneumonia", "Human / Animal Bites", "Infected Wounds"),
      contraindications = listOf("Severe penicillin allergy", "History of amoxicillin/clavulanate-associated cholestatic jaundice or hepatic dysfunction"),
      warnings = listOf("Higher rate of diarrhea due to clavulanic acid; take at the start of a meal", "Do not substitute two 250mg tablets for one 500mg tablet due to excess clavulanate"),
      sideEffects = listOf("Diarrhea / Loose stools", "Nausea & Vomiting", "Candidiasis (diaper rash, thrush)", "Skin rash"),
      administrationAdvice = "Must be administered at the start of a meal to optimize absorption and minimize gastrointestinal adverse effects.",
      reconstitutionStorage = "Reconstituted oral suspension MUST be kept refrigerated (2°C to 8°C) and discarded after 7–10 days. Do not freeze.",
      renalAdjustmentNote = "GFR 10–30 mL/min: 250/125 to 500/125 mg q12h; GFR < 10 mL/min: 250/125 to 500/125 mg q24h.",
      references = listOf(
        "American Academy of Pediatrics (AAP): Acute Otitis Media Practice Guidelines.",
        "Infectious Diseases Society of America (IDSA): Pediatric Sinusitis Guidelines.",
        "British National Formulary for Children (BNF-C) 2025–2026: Co-Amoxiclav Regimens."
      ),
      indianBrands = listOf(
        IndianBrand("Augmentin Duo", "GlaxoSmithKline (GSK)", "Oral Susp 228.5mg/5mL, DDS 457mg/5mL, ES-600mg/5mL", "30 mL / 70 mL with sterile water"),
        IndianBrand("Clavam", "Alkem Laboratories", "BD Dry Syrup 228.5mg/5mL, Forte 457mg/5mL, Drops 91.4mg/mL", "30 mL / 60 mL bottle"),
        IndianBrand("Moxikind-CV", "Mankind Pharma", "Dry Syrup 228.5mg/5mL, Forte 457mg/5mL", "30 mL / 60 mL bottle"),
        IndianBrand("Sensiclav", "Macleods Pharmaceuticals", "Dry Syrup 228.5mg/5mL, 457mg/5mL, Drops", "30 mL / 60 mL bottle"),
        IndianBrand("Advent", "Cipla Ltd", "Dry Syrup 228.5mg/5mL, Forte 457mg/5mL", "30 mL / 60 mL bottle")
      )
    ),

    // 6. CEPHALEXIN
    Drug(
      id = "cephalexin",
      name = "Cephalexin",
      genericName = "Cephalexin Monohydrate",
      category = DrugCategory.ANTIBIOTIC,
      subtitle = "First-Generation Cephalosporin",
      description = "First-generation oral cephalosporin primarily active against Gram-positive cocci (Staphylococcus aureus, Streptococcus pyogenes) for skin, bone, and urinary infections.",
      standardRegimenSummary = "25–50 mg/kg/day divided every 6 to 12 hours. Severe infections up to 75–100 mg/kg/day. Max 2000–4000 mg/day.",
      adultDoseSummary = "250–500 mg PO every 6 hours or 500 mg PO every 12 hours (Max 4000 mg/day).",
      minAgeMonths = 1,
      defaultRegimen = IndicationRegimen(
        id = "ceph_std",
        name = "Skin & Soft Tissue / UTI (Standard)",
        description = "25–50 mg/kg/day divided every 6–8 hours (or every 12 hours for uncomplicated strep/skin).",
        defaultMgPerKgPerDay = 40.0,
        frequencyTimesPerDay = 3,
        frequencyDescription = "Every 8 hours (or every 6-12 hours)",
        maxDailyDoseMg = 2000.0,
        maxSingleDoseMg = 500.0,
        standardDurationDays = 7
      ),
      alternativeRegimens = listOf(
        IndicationRegimen(
          id = "ceph_severe",
          name = "Severe / Bone & Joint Infection",
          description = "75–100 mg/kg/day divided every 6 hours. Max 4000 mg/day.",
          defaultMgPerKgPerDay = 75.0,
          frequencyTimesPerDay = 4,
          frequencyDescription = "Every 6 hours (4 times daily)",
          maxDailyDoseMg = 4000.0,
          maxSingleDoseMg = 1000.0,
          standardDurationDays = 10
        )
      ),
      formulations = listOf(
        Formulation("ceph_125", "Oral Suspension 125 mg / 5 mL", FormulationType.ORAL_SUSPENSION, 125.0, 5.0, "125mg/5mL", listOf(60.0, 100.0)),
        Formulation("ceph_250", "Oral Suspension 250 mg / 5 mL", FormulationType.ORAL_SUSPENSION, 250.0, 5.0, "250mg/5mL", listOf(60.0, 100.0)),
        Formulation("ceph_500_cap", "Capsule 500 mg", FormulationType.CAPSULE, 500.0, 1.0, "500mg Capsule")
      ),
      indications = listOf("Impetigo / Cellulitis", "Staphylococcal Skin Infections", "Streptococcal Pharyngitis", "Urinary Tract Infection", "Osteomyelitis"),
      contraindications = listOf("Severe anaphylactic reaction to cephalosporins or penicillins"),
      warnings = listOf("Cross-reactivity with penicillin allergy is ~1-3%", "Reduce dose in renal impairment"),
      sideEffects = listOf("Diarrhea", "Dyspepsia", "Nausea", "Genital candidiasis"),
      administrationAdvice = "May be taken with or without meals. Taking with food reduces stomach upset.",
      reconstitutionStorage = "Keep refrigerated after reconstitution. Discard unused portion after 14 days.",
      renalAdjustmentNote = "GFR < 50 mL/min: extend dosing intervals (q8-12h); GFR < 10 mL/min: q12-24h.",
      references = listOf(
        "Nelson Textbook of Pediatrics, 21st Edition: Cephalosporins in Pediatric Practice.",
        "British National Formulary for Children (BNF-C) 2025–2026: Cephalexin Guidelines."
      ),
      indianBrands = listOf(
        IndianBrand("Phexin", "GlaxoSmithKline (GSK)", "Drops 100mg/mL, Redidose Susp 125mg/5mL, 250mg/5mL", "10 mL / 60 mL bottle"),
        IndianBrand("Sporidex", "Sun Pharma", "Drops 100mg/mL, Redimix Susp 125mg/5mL, 250mg/5mL", "10 mL / 60 mL bottle"),
        IndianBrand("Ceff", "Lupin Ltd", "Dry Syrup 125mg/5mL, 250mg/5mL", "60 mL bottle"),
        IndianBrand("Alcephin", "Alembic Pharmaceuticals", "Oral Suspension 125mg/5mL, 250mg/5mL", "60 mL bottle")
      )
    ),

    // 7. CETIRIZINE
    Drug(
      id = "cetirizine",
      name = "Cetirizine",
      genericName = "Cetirizine Hydrochloride",
      category = DrugCategory.ANTIHISTAMINE,
      subtitle = "2nd-Generation Non-Sedating Antihistamine",
      description = "Selective peripheral H1-receptor antagonist for allergic rhinitis, chronic urticaria, and atopic eczema pruritus with rapid onset and minimal sedation.",
      standardRegimenSummary = "Age 6m–2y: 2.5 mg once daily; Age 2–5y: 2.5 mg q12-24h (max 5 mg/day); Age ≥6y & Adults: 5–10 mg once daily.",
      adultDoseSummary = "10 mg PO once daily (or 5 mg twice daily).",
      minAgeMonths = 6,
      defaultRegimen = IndicationRegimen(
        id = "cet_age_based",
        name = "Age-Calibrated Allergic Rhinitis & Urticaria",
        description = "Pediatric dosing: 6m–2y: 2.5mg/day; 2–5y: 2.5–5mg/day; ≥6y: 5–10mg/day (approx 0.25 mg/kg/day).",
        defaultMgPerKgPerDay = 0.25,
        defaultMgPerKgPerDose = 0.25,
        frequencyTimesPerDay = 1,
        frequencyDescription = "Once daily (preferably in the evening)",
        maxDailyDoseMg = 10.0,
        maxSingleDoseMg = 10.0,
        minAgeMonths = 6,
        standardDurationDays = 14
      ),
      formulations = listOf(
        Formulation("cet_5", "Oral Solution / Syrup 5 mg / 5 mL (1 mg/mL)", FormulationType.SYRUP, 5.0, 5.0, "5mg/5mL (1mg/mL)", listOf(60.0, 120.0)),
        Formulation("cet_drops", "Pediatric Drops 10 mg / 1 mL", FormulationType.DROPS, 10.0, 1.0, "10mg/1mL", listOf(15.0, 20.0)),
        Formulation("cet_10_tab", "Tablet 10 mg", FormulationType.TABLET, 10.0, 1.0, "10mg Tablet")
      ),
      indications = listOf("Allergic Rhinitis (Hay Fever)", "Chronic Urticaria (Hives)", "Allergic Conjunctivitis", "Atopic Dermatitis Pruritus"),
      contraindications = listOf("Severe hypersensitivity to cetirizine or hydroxyzine", "End-stage renal disease (CrCl < 10 mL/min)"),
      warnings = listOf("May cause mild drowsiness in sensitive individuals; avoid alcohol or CNS depressants"),
      sideEffects = listOf("Mild somnolence / drowsiness", "Dry mouth", "Headache", "Fatigue"),
      administrationAdvice = "Can be taken with or without food. Evening dose is recommended if drowsiness occurs.",
      reconstitutionStorage = "Store at room temperature 15°C to 30°C.",
      renalAdjustmentNote = "Moderate renal impairment (CrCl 11–31 mL/min): reduce dose by 50%.",
      references = listOf(
        "World Health Organization (WHO) Model List of Essential Medicines for Children (EMLc) 9th List (2023): Cetirizine.",
        "British National Formulary for Children (BNF-C) 2025–2026: Cetirizine in Allergic Conditions.",
        "Nelson Textbook of Pediatrics, 21st Edition: Antihistamines in Pediatric Atopic Diseases."
      ),
      indianBrands = listOf(
        IndianBrand("Cetzine", "Dr. Reddy's Laboratories", "Syrup 5mg/5mL (1mg/mL), Drops 10mg/mL", "30 mL / 60 mL bottle"),
        IndianBrand("Alerid", "Cipla Ltd", "Syrup 5mg/5mL, Paediatric Drops 10mg/mL", "30 mL / 60 mL bottle"),
        IndianBrand("Okacet", "Cipla Ltd", "Syrup 5mg/5mL, Drops 10mg/mL", "30 mL / 60 mL bottle"),
        IndianBrand("Zyrtec", "Dr. Reddy's / UCB", "Syrup 5mg/5mL, Drops 10mg/mL", "30 mL / 60 mL bottle"),
        IndianBrand("Incid-L", "Bayer / Zydus Healthcare", "Syrup 5mg/5mL", "60 mL bottle")
      )
    ),

    // 8. SALBUTAMOL / ALBUTEROL
    Drug(
      id = "salbutamol",
      name = "Salbutamol (Albuterol)",
      genericName = "Salbutamol Sulfate",
      category = DrugCategory.RESPIRATORY,
      subtitle = "Short-Acting Beta-2 Agonist (Bronchodilator)",
      description = "Selective beta-2 adrenergic agonist for rapid relief of bronchospasm in asthma, acute bronchitis, wheezing, and reversible obstructive airway disease.",
      standardRegimenSummary = "Oral: 0.1–0.15 mg/kg/dose every 6–8 hours. Inhaled MDI: 1–2 puffs q4-6h with spacer. Max 0.3 mg/kg/day oral (max 12–16 mg/day).",
      adultDoseSummary = "Oral: 2–4 mg PO 3–4 times daily (Max 32 mg/day). MDI: 100–200 mcg (1–2 puffs) q4-6h PRN.",
      minAgeMonths = 6,
      defaultRegimen = IndicationRegimen(
        id = "salb_std_oral",
        name = "Acute Bronchospasm / Wheezing (Oral Syrup)",
        description = "0.1–0.15 mg/kg/dose given 3 times daily (every 8 hours) as needed.",
        defaultMgPerKgPerDay = 0.35,
        defaultMgPerKgPerDose = 0.12,
        frequencyTimesPerDay = 3,
        frequencyDescription = "Every 8 hours (3 times daily) as needed",
        maxDailyDoseMg = 12.0,
        maxSingleDoseMg = 4.0,
        minAgeMonths = 6,
        standardDurationDays = 5
      ),
      formulations = listOf(
        Formulation("salb_2", "Syrup 2 mg / 5 mL (0.4 mg/mL)", FormulationType.SYRUP, 2.0, 5.0, "2mg/5mL", listOf(60.0, 100.0)),
        Formulation("salb_2_tab", "Tablet 2 mg", FormulationType.TABLET, 2.0, 1.0, "2mg Tablet"),
        Formulation("salb_4_tab", "Tablet 4 mg", FormulationType.TABLET, 4.0, 1.0, "4mg Tablet")
      ),
      indications = listOf("Bronchial Asthma Acute Relief", "Wheeze-Associated Respiratory Infections", "Bronchospasm", "Exercise-Induced Bronchospasm"),
      contraindications = listOf("Hypersensitivity to salbutamol", "Uncontrolled tachyarrhythmias"),
      warnings = listOf("Note: Inhaled aerosol with spacer is clinically preferred over oral syrup whenever possible for faster relief and fewer systemic side effects", "High doses may cause hypokalemia and tachycardia"),
      sideEffects = listOf("Fine tremor (especially hands)", "Tachycardia / Palpitations", "Nervousness / Restlessness", "Headache"),
      administrationAdvice = "Administer on an empty or full stomach. If heart palpitations or intense jitters occur, consult prescriber.",
      reconstitutionStorage = "Store at room temperature below 25°C. Protect from light.",
      renalAdjustmentNote = "No specific adjustment needed.",
      references = listOf(
        "Global Initiative for Asthma (GINA) 2024 Guidelines: Pediatric Asthma Management.",
        "British National Formulary for Children (BNF-C) 2025–2026: Salbutamol Bronchodilator Protocols.",
        "Nelson Textbook of Pediatrics, 21st Edition: Beta-2 Agonists in Wheezing and Asthma."
      ),
      indianBrands = listOf(
        IndianBrand("Asthalin", "Cipla Ltd", "Syrup 2mg/5mL, Respules 2.5mg, Inhaler 100mcg", "100 mL bottle"),
        IndianBrand("Ventorlin", "GlaxoSmithKline (GSK)", "Syrup 2mg/5mL, Inhaler 100mcg", "100 mL bottle"),
        IndianBrand("Ascoril", "Glenmark Pharmaceuticals", "Syrup (Salbutamol single and expectorant forms)", "100 mL bottle"),
        IndianBrand("Bronkosol", "Cadila Pharmaceuticals", "Syrup 2mg/5mL", "100 mL bottle")
      )
    ),

    // 9. ONDANSETRON
    Drug(
      id = "ondansetron",
      name = "Ondansetron",
      genericName = "Ondansetron Hydrochloride",
      category = DrugCategory.GASTROINTESTINAL,
      subtitle = "5-HT3 Receptor Antagonist (Antiemetic)",
      description = "Potent serotonin 5-HT3 antagonist for acute gastroenteritis vomiting in children, chemotherapy-induced nausea, and post-operative nausea/vomiting.",
      standardRegimenSummary = "Weight-based: 0.15 mg/kg/dose PO every 8 hours PRN (or age-based: 8–15kg = 2mg, 15–30kg = 4mg, >30kg = 8mg). Max 8 mg/single dose.",
      adultDoseSummary = "4–8 mg PO every 8 hours as needed.",
      minAgeMonths = 6,
      defaultRegimen = IndicationRegimen(
        id = "ond_gastro",
        name = "Acute Gastroenteritis Vomiting (Oral Rehydration)",
        description = "0.15 mg/kg single dose to facilitate oral rehydration therapy (ORT). Repeat q8h if needed.",
        defaultMgPerKgPerDay = 0.45,
        defaultMgPerKgPerDose = 0.15,
        frequencyTimesPerDay = 3,
        frequencyDescription = "Every 8 hours as needed for vomiting (1-3 doses typically)",
        maxDailyDoseMg = 24.0,
        maxSingleDoseMg = 8.0,
        minAgeMonths = 6,
        standardDurationDays = 2
      ),
      formulations = listOf(
        Formulation("ond_4", "Oral Solution 4 mg / 5 mL (0.8 mg/mL)", FormulationType.SYRUP, 4.0, 5.0, "4mg/5mL", listOf(50.0)),
        Formulation("ond_4_odt", "Orally Disintegrating Tablet (ODT) 4 mg", FormulationType.TABLET, 4.0, 1.0, "4mg ODT Tablet"),
        Formulation("ond_8_tab", "Tablet 8 mg", FormulationType.TABLET, 8.0, 1.0, "8mg Tablet")
      ),
      indications = listOf("Acute Gastroenteritis Vomiting", "Chemotherapy-Induced Nausea & Vomiting", "Post-Operative Nausea & Vomiting"),
      contraindications = listOf("Concomitant use of apomorphine (causes profound hypotension)", "Congenital long QT syndrome"),
      warnings = listOf("Dose-dependent QT prolongation risk", "Do not use to treat simple regurgitation or without clinical evaluation"),
      sideEffects = listOf("Headache", "Constipation", "Transient flushing", "Dizziness"),
      administrationAdvice = "Give 15–30 minutes before oral rehydration solution (ORS) fluids to allow gut absorption.",
      reconstitutionStorage = "Store at room temperature 15°C to 30°C. Protect from light.",
      renalAdjustmentNote = "No adjustment for renal impairment. In severe hepatic impairment (Child-Pugh C), max dose is 8 mg/day.",
      references = listOf(
        "European Society for Paediatric Gastroenterology, Hepatology and Nutrition (ESPGHAN) Guidelines: Management of Acute Gastroenteritis in Children.",
        "American Academy of Pediatrics (AAP): Antiemetic Therapy in Acute Gastroenteritis.",
        "British National Formulary for Children (BNF-C) 2025–2026: Ondansetron Dosing."
      ),
      indianBrands = listOf(
        IndianBrand("Emeset", "Cipla Ltd", "Syrup 2mg/5mL, Drops 2mg/mL, ODT 4mg, Inj 2mg/mL", "30 mL / 50 mL bottle"),
        IndianBrand("Ondem", "Alkem Laboratories", "Syrup 2mg/5mL, Drops 2mg/mL, MD 4mg", "30 mL bottle"),
        IndianBrand("Vomikind", "Mankind Pharma", "Syrup 2mg/5mL, Fast Strip 4mg, MD 4mg", "30 mL bottle"),
        IndianBrand("Periset", "Ipca Laboratories", "Syrup 2mg/5mL, Drops 2mg/mL", "30 mL bottle"),
        IndianBrand("Zofer", "Sun Pharma", "Oral Solution 2mg/5mL, ODT 4mg", "30 mL bottle")
      )
    ),

    // 10. PREDNISOLONE
    Drug(
      id = "prednisolone",
      name = "Prednisolone",
      genericName = "Prednisolone Sodium Phosphate",
      category = DrugCategory.CORTICOSTEROID,
      subtitle = "Systemic Glucocorticoid (Anti-inflammatory)",
      description = "Intermediate-acting corticosteroid for acute asthma exacerbation, severe allergic reactions, croup, nephrotic syndrome, and inflammatory states.",
      standardRegimenSummary = "Acute Asthma Exacerbation: 1–2 mg/kg/day (single dose or divided q12h) for 3–5 days. Max 60 mg/day. No taper needed for short courses (< 7 days).",
      adultDoseSummary = "40–60 mg PO once daily for 5 days.",
      minAgeMonths = 1,
      defaultRegimen = IndicationRegimen(
        id = "pred_asthma_burst",
        name = "Acute Asthma Flare / Wheeze Burst (3-5 Days)",
        description = "1–2 mg/kg/day given as a single morning dose or divided twice daily for 3 to 5 days.",
        defaultMgPerKgPerDay = 1.5,
        defaultMgPerKgPerDose = 1.5,
        frequencyTimesPerDay = 1,
        frequencyDescription = "Once daily in the morning with breakfast",
        maxDailyDoseMg = 60.0,
        maxSingleDoseMg = 60.0,
        standardDurationDays = 5
      ),
      alternativeRegimens = listOf(
        IndicationRegimen(
          id = "pred_croup",
          name = "Croup (Laryngotracheobronchitis)",
          description = "1 mg/kg single oral dose.",
          defaultMgPerKgPerDay = 1.0,
          defaultMgPerKgPerDose = 1.0,
          frequencyTimesPerDay = 1,
          frequencyDescription = "Single dose (or repeat once in 24h if severe)",
          maxDailyDoseMg = 40.0,
          maxSingleDoseMg = 40.0,
          standardDurationDays = 1
        )
      ),
      formulations = listOf(
        Formulation("pred_5", "Oral Liquid 5 mg / 5 mL (1 mg/mL)", FormulationType.SYRUP, 5.0, 5.0, "5mg/5mL", listOf(60.0, 120.0)),
        Formulation("pred_15", "Oral Solution 15 mg / 5 mL (3 mg/mL - Prelone)", FormulationType.SYRUP, 15.0, 5.0, "15mg/5mL (3mg/mL)", listOf(60.0, 120.0)),
        Formulation("pred_5_tab", "Tablet 5 mg", FormulationType.TABLET, 5.0, 1.0, "5mg Tablet"),
        Formulation("pred_20_tab", "Tablet 20 mg", FormulationType.TABLET, 20.0, 1.0, "20mg Tablet")
      ),
      indications = listOf("Acute Asthma Exacerbation", "Croup (Laryngotracheobronchitis)", "Nephrotic Syndrome", "Severe Allergic Reactions", "Juvenile Rheumatoid Arthritis"),
      contraindications = listOf("Systemic untreated fungal infections", "Live virus vaccination during immunosuppressive doses"),
      warnings = listOf("Take in morning with food to mimic natural cortisol and reduce gastric irritation", "Short burst (< 7 days) does not require dose tapering"),
      sideEffects = listOf("Increased appetite / transient hyperactivity", "Mild stomach discomfort", "Facial flushing", "Mood irritability"),
      administrationAdvice = "Administer in the morning with milk or breakfast to reduce GI irritation and minimize sleep disruption.",
      reconstitutionStorage = "Store at controlled room temperature 20°C to 25°C. Do not refrigerate.",
      renalAdjustmentNote = "No specific dosage adjustments required.",
      references = listOf(
        "Global Initiative for Asthma (GINA) 2024 Guidelines: Systemic Corticosteroids in Acute Pediatric Asthma.",
        "British National Formulary for Children (BNF-C) 2025–2026: Prednisolone Dosing Protocols.",
        "Nelson Textbook of Pediatrics, 21st Edition: Corticosteroid Therapy."
      ),
      indianBrands = listOf(
        IndianBrand("Omnacortil", "Macleods Pharmaceuticals", "Drops 5mg/mL, Oral Susp 5mg/5mL, Forte 15mg/5mL", "30 mL / 60 mL bottle"),
        IndianBrand("Wysolone", "Pfizer India", "Dispersible Tablets 5mg, 10mg, 20mg", "Strip of 15 tablets"),
        IndianBrand("Kidpred", "Alkem Laboratories", "Syrup 5mg/5mL, 15mg/5mL", "30 mL / 60 mL bottle"),
        IndianBrand("Predone", "Cipla Ltd", "Syrup 5mg/5mL, 15mg/5mL", "30 mL / 60 mL bottle")
      )
    ),

    // 11. CEFIXIME
    Drug(
      id = "cefixime",
      name = "Cefixime",
      genericName = "Cefixime Trihydrate",
      category = DrugCategory.ANTIBIOTIC,
      subtitle = "3rd-Generation Cephalosporin (Broad Gram-Negative)",
      description = "Oral third-generation cephalosporin with excellent activity against Gram-negative enterics, used for pediatric UTI, acute otitis media, and typhoid/enteric fever.",
      standardRegimenSummary = "8 mg/kg/day PO as a single daily dose or divided every 12 hours (4 mg/kg q12h). Typhoid: 15–20 mg/kg/day. Max 400 mg/day.",
      adultDoseSummary = "400 mg PO once daily or 200 mg PO every 12 hours.",
      minAgeMonths = 6,
      defaultRegimen = IndicationRegimen(
        id = "cefix_std",
        name = "Standard Pediatric UTI / Otitis Media",
        description = "8 mg/kg/day as a single dose or divided into two doses (4 mg/kg every 12h).",
        defaultMgPerKgPerDay = 8.0,
        frequencyTimesPerDay = 2,
        frequencyDescription = "Every 12 hours (or once daily)",
        maxDailyDoseMg = 400.0,
        maxSingleDoseMg = 400.0,
        minAgeMonths = 6,
        standardDurationDays = 7
      ),
      alternativeRegimens = listOf(
        IndicationRegimen(
          id = "cefix_typhoid",
          name = "Enteric / Typhoid Fever Protocol",
          description = "15–20 mg/kg/day divided every 12 hours for 14 days (Max 800 mg/day).",
          defaultMgPerKgPerDay = 16.0,
          frequencyTimesPerDay = 2,
          frequencyDescription = "Every 12 hours for 10-14 days",
          maxDailyDoseMg = 800.0,
          maxSingleDoseMg = 400.0,
          minAgeMonths = 6,
          standardDurationDays = 14
        )
      ),
      formulations = listOf(
        Formulation("cefix_100", "Oral Suspension 100 mg / 5 mL", FormulationType.ORAL_SUSPENSION, 100.0, 5.0, "100mg/5mL", listOf(30.0, 50.0)),
        Formulation("cefix_200_susp", "Oral Suspension 200 mg / 5 mL", FormulationType.ORAL_SUSPENSION, 200.0, 5.0, "200mg/5mL", listOf(50.0)),
        Formulation("cefix_200_tab", "Tablet 200 mg", FormulationType.TABLET, 200.0, 1.0, "200mg Tablet"),
        Formulation("cefix_400_tab", "Tablet 400 mg", FormulationType.TABLET, 400.0, 1.0, "400mg Tablet")
      ),
      indications = listOf("Urinary Tract Infections (UTI)", "Acute Otitis Media", "Typhoid (Enteric) Fever", "Pharyngitis / Tonsillitis"),
      contraindications = listOf("Known severe hypersensitivity to cephalosporins"),
      warnings = listOf("Suspension produces higher peak blood levels than tablets at identical doses; do not interchange without dose recalculation"),
      sideEffects = listOf("Diarrhea / Loose stool", "Abdominal pain", "Nausea", "Headache"),
      administrationAdvice = "May be administered without regard to meals. Liquid form should be shaken thoroughly before pouring.",
      reconstitutionStorage = "Reconstituted suspension is stable for 14 days at room temperature or refrigerated. Keep tightly closed.",
      renalAdjustmentNote = "CrCl 20–60 mL/min: give 75% of standard dose; CrCl < 20 mL/min: give 50% of standard dose.",
      references = listOf(
        "World Health Organization (WHO): Guidelines for the Management of Typhoid Fever.",
        "American Academy of Pediatrics (AAP) Subcommittee on UTI: Diagnosis and Management of Initial UTI in Febrile Infants and Young Children. Pediatrics. 2011;128(3):595-610.",
        "British National Formulary for Children (BNF-C) 2025–2026: Cefixime Dosing."
      ),
      indianBrands = listOf(
        IndianBrand("Taxim-O", "Alkem Laboratories", "Drops 25mg/mL, Dry Syrup 50mg/5mL, 100mg/5mL, Forte 200mg/5mL", "30 mL bottle with sterile water"),
        IndianBrand("Zifi", "FDC Limited", "Drops 25mg/mL, Dry Syrup 50mg/5mL, 100mg/5mL, 200mg/5mL", "30 mL bottle with sterile water"),
        IndianBrand("Mahacef", "Mankind Pharma", "Dry Syrup 50mg/5mL, 100mg/5mL", "30 mL bottle"),
        IndianBrand("Cefspan", "Sanofi / GlaxoSmithKline", "Oral Suspension 100mg/5mL", "30 mL bottle"),
        IndianBrand("Omnicef-O", "Aristo Pharmaceuticals", "Dry Syrup 50mg/5mL, 100mg/5mL", "30 mL bottle")
      )
    ),

    // 12. METRONIDAZOLE
    Drug(
      id = "metronidazole",
      name = "Metronidazole",
      genericName = "Metronidazole Benzoate",
      category = DrugCategory.ANTIBIOTIC,
      subtitle = "Antiprotozoal & Anaerobic Antibacterial",
      description = "Nitroimidazole antimicrobial active against obligate anaerobic bacteria (Bacteroides, Clostridium) and protozoa (Giardia lamblia, Entamoeba histolytica, Trichomonas).",
      standardRegimenSummary = "Giardiasis: 15 mg/kg/day divided q8h for 5–7 days. Amebiasis: 35–50 mg/kg/day divided q8h for 7–10 days. Anaerobic infections: 30 mg/kg/day divided q8h.",
      adultDoseSummary = "500 mg PO every 8 hours for 7–10 days.",
      minAgeMonths = 1,
      defaultRegimen = IndicationRegimen(
        id = "metro_giardia",
        name = "Giardiasis / Intestinal Protozoa",
        description = "15 mg/kg/day divided every 8 hours (q8h) for 5 to 7 days. Max 750 mg/day.",
        defaultMgPerKgPerDay = 15.0,
        frequencyTimesPerDay = 3,
        frequencyDescription = "Every 8 hours (3 times daily) with meals",
        maxDailyDoseMg = 750.0,
        maxSingleDoseMg = 250.0,
        standardDurationDays = 5
      ),
      alternativeRegimens = listOf(
        IndicationRegimen(
          id = "metro_ameba",
          name = "Amebic Dysentery / Anaerobic Infection",
          description = "35–50 mg/kg/day divided every 8 hours for 7–10 days. Max 2250 mg/day.",
          defaultMgPerKgPerDay = 40.0,
          frequencyTimesPerDay = 3,
          frequencyDescription = "Every 8 hours with meals for 7-10 days",
          maxDailyDoseMg = 2250.0,
          maxSingleDoseMg = 750.0,
          standardDurationDays = 10
        )
      ),
      formulations = listOf(
        Formulation("metro_200", "Oral Suspension 200 mg / 5 mL (Benzoate)", FormulationType.ORAL_SUSPENSION, 200.0, 5.0, "200mg/5mL", listOf(60.0, 100.0)),
        Formulation("metro_250_tab", "Tablet 250 mg", FormulationType.TABLET, 250.0, 1.0, "250mg Tablet"),
        Formulation("metro_400_tab", "Tablet 400 mg", FormulationType.TABLET, 400.0, 1.0, "400mg Tablet")
      ),
      indications = listOf("Giardiasis (Watery Diarrhea)", "Intestinal Amebiasis / Dysentery", "Anaerobic Intra-abdominal Infections", "C. difficile-associated Diarrhea", "Bacterial Vaginosis"),
      contraindications = listOf("Hypersensitivity to metronidazole or other nitroimidazoles", "First trimester of pregnancy (relative)"),
      warnings = listOf("Disulfiram-like ethanol reaction: strictly avoid alcohol-containing medicines or food during and 48h after therapy", "Metallic taste is common"),
      sideEffects = listOf("Metallic taste in mouth", "Nausea / Anorexia", "Darkened reddish-brown urine (harmless)", "Mild dizziness"),
      administrationAdvice = "Take with or after food to decrease stomach irritation. Metronidazole benzoate suspension tastes much better than crushed tablets.",
      reconstitutionStorage = "Store below 25°C. Protect from direct sunlight.",
      renalAdjustmentNote = "Severe renal impairment (GFR < 10 mL/min): reduce dose by 50%.",
      references = listOf(
        "World Health Organization (WHO) Guidelines: Pharmacological Management of Amebiasis and Giardiasis in Children.",
        "British National Formulary for Children (BNF-C) 2025–2026: Metronidazole in Pediatric Infections.",
        "Nelson Textbook of Pediatrics, 21st Edition: Antiprotozoal Chemotherapy."
      ),
      indianBrands = listOf(
        IndianBrand("Flagyl", "Abbott Healthcare / Sanofi", "Suspension 200mg/5mL (as Benzoate)", "60 mL bottle"),
        IndianBrand("Metrogyl", "J.B. Chemicals & Pharmaceuticals", "Suspension 200mg/5mL, Tablets 200/400mg", "60 mL bottle"),
        IndianBrand("Aristogyl", "Aristo Pharmaceuticals", "Suspension 200mg/5mL", "60 mL bottle")
      )
    )
  )

  fun findDrugById(id: String): Drug? = drugs.find { it.id.equals(id, ignoreCase = true) }

  fun searchDrugs(query: String, category: DrugCategory): List<Drug> {
    return drugs.filter { drug ->
      val matchesCategory = category == DrugCategory.ALL || drug.category == category
      val matchesQuery = query.isBlank() ||
        drug.name.contains(query, ignoreCase = true) ||
        drug.genericName.contains(query, ignoreCase = true) ||
        drug.subtitle.contains(query, ignoreCase = true) ||
        drug.indications.any { it.contains(query, ignoreCase = true) }
      matchesCategory && matchesQuery
    }
  }

  // Calculate estimated weight from age using standard clinical formulas (Nelson / APLS)
  fun estimateWeightKg(ageYears: Float, ageMonths: Int): Double {
    val totalMonths = (ageYears * 12 + ageMonths).toInt()
    return when {
      totalMonths <= 0 -> 3.5
      totalMonths in 1..11 -> (totalMonths + 9.0) / 2.0 // Infant rule: (Months + 9) / 2
      totalMonths in 12..60 -> {
        // 1 - 5 years: 2 * (Years + 5)
        val years = totalMonths / 12.0
        2.0 * (years + 5.0)
      }
      totalMonths in 61..144 -> {
        // 6 - 12 years: 3 * Years + 7
        val years = totalMonths / 12.0
        3.0 * years + 7.0
      }
      else -> {
        // Adolescent / Adult default approximation
        val years = totalMonths / 12.0
        minOf(70.0, 3.5 * years - 5.0)
      }
    }
  }
}
