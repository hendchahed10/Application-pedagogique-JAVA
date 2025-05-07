package com.example.projet.service;

import java.io.File;
import java.util.List;

import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public class RecommendationService {

    private final DataModel model;

    public RecommendationService() {
        try {
            System.out.println("Starting RecommendationService setup...");
            String dbPath = "C:/IdeaProjects/Javads2/database.db";
            String csvPath = "src/main/resources/Avis.csv";
            CSVExporter.exportAvisToCSV(dbPath, csvPath);
            File ratingsFile = new File(csvPath);
            System.out.println("📄 CSV exists: " + ratingsFile.exists());
            this.model = new FileDataModel(ratingsFile);
            // ...
        } catch (Exception e) {
            System.err.println("🔥 Initialization failed: ");
            e.printStackTrace();
            throw new RuntimeException(e); // Prevent silent failures
        }
    }

    public List<RecommendedItem> getRecommendations(long userId, int howMany) throws Exception {
        System.out.println(" Getting recommendations for user ID: " + userId);

        // Show all user IDs
        LongPrimitiveIterator userIterator = model.getUserIDs();
        System.out.print(" Existing user IDs in model: ");
        while (userIterator.hasNext()) {
            System.out.print(userIterator.nextLong() + " ");
        }
        System.out.println();

        // Show all ratings for debugging
        System.out.println("Showing all user-item-rating triples:");
        LongPrimitiveIterator users = model.getUserIDs();
        while (users.hasNext()) {
            long uid = users.nextLong();
            LongPrimitiveIterator items = model.getItemIDsFromUser(uid).iterator();
            while (items.hasNext()) {
                long iid = items.nextLong();
                double rating = model.getPreferenceValue(uid, iid);
                System.out.println("User: " + uid + " | Item: " + iid + " | Rating: " + rating);
            }
        }

        // Calculate item similarities
        ItemSimilarity similarity = new LogLikelihoodSimilarity(model);
        GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(model, similarity);

        System.out.println(" Calculating item similarities...");
        LongPrimitiveIterator itemIterator1 = model.getItemIDs();
        while (itemIterator1.hasNext()) {
            long itemId1 = itemIterator1.nextLong();
            LongPrimitiveIterator itemIterator2 = model.getItemIDs();
            while (itemIterator2.hasNext()) {
                long itemId2 = itemIterator2.nextLong();
                if (itemId1 != itemId2) {
                    double sim = similarity.itemSimilarity(itemId1, itemId2);
                    System.out.println("Similarity between item " + itemId1 + " and item " + itemId2 + " = " + sim);
                }
            }
        }

        System.out.println(" Generating recommendations...");
        List<RecommendedItem> recs = recommender.recommend(userId, howMany);

        if (recs.isEmpty()) {
            System.out.println("No recommendations found for user ID " + userId);
        } else {
            System.out.println("Recommendations for user ID " + userId + ":");
            for (RecommendedItem item : recs) {
                System.out.println("➡ Item ID: " + item.getItemID() + " | Score: " + item.getValue());
            }
        }

        return recs;
    }
}
