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
                "British National Formulary for Children (BNF-C) 2025–2026: Paracetamol / Acetaminophen Pediatric Regimens."
            ),
            indianBrands = listOf(
                IndianBrand("Calpol", "GlaxoSmithKline (GSK)", "Paediatric Drops 100mg/mL, Pead Susp 120mg/5mL, Calpol 250 Plus Susp", "15 mL / 60 mL / 120 mL"),
                IndianBrand("Dolo", "Micro Labs Ltd", "Infant Drops 100mg/mL, Susp 120mg/5mL, Dolo-250 Susp 250mg/5mL", "15 mL / 60 mL"),
                IndianBrand("Crocin", "Haleon / GSK", "Baby Drops 100mg/mL, Syrup 120mg/5mL, Crocin DS 240mg/5mL", "15 mL / 60 mL")
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
                )
            ),
            formulations = listOf(
                Formulation("ibu_100", "Oral Suspension 100 mg / 5 mL", FormulationType.ORAL_SUSPENSION, 100.0, 5.0, "100mg/5mL", listOf(60.0, 100.0, 150.0)),
                Formulation("ibu_200_susp", "Forte Suspension 200 mg / 5 mL", FormulationType.ORAL_SUSPENSION, 200.0, 5.0, "200mg/5mL", listOf(100.0)),
                Formulation("ibu_200_tab", "Tablet 200 mg", FormulationType.TABLET, 200.0, 1.0, "200mg Tablet")
            ),
            indications = listOf("Fever unresponsive to paracetamol", "Inflammatory pain", "Otitis media pain"),
            contraindications = listOf("Infants younger than 3 months or < 5 kg", "Active GI bleeding or ulceration"),
            warnings = listOf("Ensure patient is adequately hydrated to prevent NSAID nephrotoxicity", "Administer with food or milk to minimize GI irritation"),
            sideEffects = listOf("Gastric upset / dyspepsia", "Nausea", "Abdominal discomfort"),
            administrationAdvice = "Always take with food, milk, or immediately after a meal to protect the stomach lining.",
            reconstitutionStorage = "Store at room temperature. Shake bottle thoroughly before each administration.",
            renalAdjustmentNote = "Avoid in acute kidney injury or severe renal impairment.",
            references = listOf(
                "American Academy of Pediatrics (AAP): Management of Pediatric Fever and Pain."
            ),
            indianBrands = listOf(
                IndianBrand("Ibugesic", "Cipla Ltd", "Oral Suspension 100mg/5mL", "60 mL bottle"),
                IndianBrand("Brufen", "Abbott India", "Junior Syrup 100mg/5mL", "60 mL bottle")
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
                Formulation("azith_250_tab", "Tablet 250 mg", FormulationType.TABLET, 250.0, 1.0, "250mg Tablet")
            ),
            indications = listOf("Community-Acquired Pneumonia", "Acute Bacterial Sinusitis", "Pertussis Treatment/Prophylaxis"),
            contraindications = listOf("Hypersensitivity to azithromycin or any macrolide", "History of cholestatic jaundice with prior macrolide use"),
            warnings = listOf("May cause QT prolongation", "Infantile hypertrophic pyloric stenosis reported in neonates"),
            sideEffects = listOf("Diarrhea", "Nausea", "Vomiting"),
            administrationAdvice = "Can be taken with or without food. Taking with food reduces GI discomfort.",
            reconstitutionStorage = "Store dry powder at room temperature. After reconstitution, stable for 10 days.",
            renalAdjustmentNote = "No dosage adjustment needed for mild-moderate renal impairment.",
            references = listOf(
                "CDC Guidelines for the Prevention and Control of Pertussis."
            ),
            indianBrands = listOf(
                IndianBrand("Azithral", "Alembic Pharmaceuticals", "Liquid 100mg/5mL, 200mg/5mL", "15 mL / 30 mL bottle"),
                IndianBrand("Azee", "Cipla Ltd", "Dry Syrup 100mg/5mL, 200mg/5mL", "15 mL / 30 mL bottle")
            )
        ),

        // 5. AUGMENTIN (CO-AMOXICLAV)
        Drug(
            id = "augmentin",
            name = "Amoxicillin / Clavulanate (Augmentin)",
            genericName = "Amoxicillin + Potassium Clavulanate",
            category = DrugCategory.ANTIBIOTIC,
            subtitle = "Beta-Lactamase Inhibitor Combination",
            description = "Broad-spectrum beta-lactam antibiotic used for otitis media, sinusitis, skin infections, and urinary tract infections where beta-lactamase producing organisms are suspected.",
            standardRegimenSummary = "30-40 mg/kg/day (based on amoxicillin component) divided every 8 hours or 45 mg/kg/day divided every 12 hours.",
            adultDoseSummary = "500/125 mg PO every 8 hours or 875/125 mg PO every 12 hours.",
            minAgeMonths = 2,
            defaultRegimen = IndicationRegimen(
                id = "aug_std",
                name = "Standard Infection",
                description = "45 mg/kg/day (amoxicillin) divided every 12 hours.",
                defaultMgPerKgPerDay = 45.0,
                frequencyTimesPerDay = 2,
                frequencyDescription = "Every 12 hours",
                maxDailyDoseMg = 1750.0,
                maxSingleDoseMg = 875.0,
                standardDurationDays = 7
            ),
            alternativeRegimens = emptyList(),
            formulations = listOf(
                Formulation("aug_228", "Dry Syrup 228.5 mg / 5 mL (200mg Amox + 28.5mg Clav)", FormulationType.ORAL_SUSPENSION, 200.0, 5.0, "200mg/5mL", listOf(30.0)),
                Formulation("aug_375", "Tablet 375 mg (250mg Amox + 125mg Clav)", FormulationType.TABLET, 250.0, 1.0, "250mg/Tab", emptyList())
            )
)
)
)