
package com.inn.foresight.indexlucene;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy;
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.apache.lucene.spatial.query.SpatialArgs;
import org.apache.lucene.spatial.query.SpatialOperation;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.jts.JtsSpatialContext;
import org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory;
import org.locationtech.spatial4j.io.WKTReader;
import org.locationtech.spatial4j.shape.Shape;

import com.inn.foresight.indexlucene.hdfs.HdfsDirectory;
import com.inn.foresight.indexlucene.hdfs.LuceneConfiguration;

public class SpatialIndexSearcher extends LuceneConfiguration {

    private IndexSearcher searcher;
    
    private static SpatialContext ctx;
    
    private static SpatialStrategy strategy;

    private static final Logger logger = LogManager.getLogger(SpatialIndexSearcher.class);

    private static Map<String, IndexSearcher> indexReaderMap = new HashMap<>();

    private static Map<String, String> tableIndexMap = new HashMap<>();
    
    private static FileSystem fileSystem;    
    static {
        logger.info("Initializing context,strategy and configuration ");
        ctx = SpatialContext.GEO;
        SpatialPrefixTree grid = new GeohashPrefixTree(ctx, 11);
        strategy = new RecursivePrefixTreeStrategy(grid, "location");        
    }
    
   
    public SpatialIndexSearcher(){	
        if(fileSystem==null) {
			try {
				fileSystem = getFileSystem();
			} catch (IOException e) {
			   logger.error("Error while creating FileSystem {}",ExceptionUtils.getStackTrace(e));	
			}
        }
	}
    
    public void setSearchIndexPath(String indexPath, String indexDir) throws IOException {
    	try {
            if (tableIndexMap.containsKey(indexDir)) {
            	logger.info("index directory {}",indexDir);
                String indexPathOld = tableIndexMap.get(indexDir);
                if (indexPathOld.equals(indexPath)) {
                    this.searcher =indexReaderMap.get(indexDir);
                    logger.info("indexReaderMap {} tableIndexMap {}",indexReaderMap.size(),tableIndexMap.size());
                } else {
                    logger.info("IndexPath is changed, oldIndexpath :- {},newIndexPath :- {} ", indexPathOld,indexPath);
                    initializeIndexSearcher(indexPath, indexDir);
                }
            } else {
            	 initializeIndexSearcher(indexPath, indexDir);
                 logger.info("initializing indexReaderMap {} tableIndexMap {}",indexReaderMap.size(),tableIndexMap.size());
            }
        } catch (Exception e) {
            logger.error("Error occured while setting index path: {} and trace is :{}", indexPath,
                    ExceptionUtils.getStackTrace(e));
        }
    	
    }

    /**
     * @param indexPath
     * @param indexDir
     * @throws IOException
     */
    private void initializeIndexSearcher(String indexPath, String indexDir) throws IOException {
        logger.info("initializeIndexSearcher without cache {} and fileSystem changes",indexDir);        
        HdfsDirectory hdfsDirectory = new HdfsDirectory(new Path(indexPath),fileSystem);
         DirectoryReader readerToUse =DirectoryReader.open(hdfsDirectory);
         this.searcher = new IndexSearcher(readerToUse);                      
//         this.searcher.setQueryCache(IndexSearcher.getDefaultQueryCache());         
//         this.searcher.setQueryCachingPolicy(IndexSearcher.getDefaultQueryCachingPolicy());         
//         this.searcher.setSimilarity(IndexSearcher.getDefaultSimilarity());
         indexReaderMap.put(indexDir, this.searcher);
         tableIndexMap.put(indexDir, indexPath);
    }

    public List<String> getRowKeyListByViewPort(Double minLng, Double minLat, Double maxLng, Double maxLat,
            String searchableField) throws IOException {
//        logger.info("Getting rowkey list within:- minLng {}, minLat {}, maxLng {}, maxLat {}", minLng, minLat, maxLng,maxLat);
        WKTReader wktReader = new WKTReader(JtsSpatialContext.GEO, new JtsSpatialContextFactory());
        Shape shape = wktReader.readIfSupported("ENVELOPE(" + minLng + "," + maxLng + "," + maxLat + "," + minLat + ")");
        // SpatialArgs args = new SpatialArgs(SpatialOperation.Intersects,ctx.makeRectangle(minLng,maxLng, minLat, maxLat));
        SpatialArgs args = new SpatialArgs(SpatialOperation.Intersects, shape);
        return getRowKeyList(args, searchableField);
    }
    
    public List<String> getRowKeyListByViewPort(String polygon ,String searchableField)throws IOException {
        WKTReader wktReader = new WKTReader(JtsSpatialContext.GEO, new JtsSpatialContextFactory());
        Shape shape = wktReader.readIfSupported(polygon);
        SpatialArgs args = new SpatialArgs(SpatialOperation.Intersects, shape);
        return getRowKeyList(args, searchableField);
    }

    public List<String> getRowKeyListByPoint(Double lat, Double lon, String searchableField, String tableName)
            throws IOException, ParseException {
        WKTReader wktReader = new WKTReader(JtsSpatialContext.GEO, new JtsSpatialContextFactory());
        Shape shape = wktReader.parseIfSupported("POINT(" + lon + " " + lat + ")");
//        logger.info("shape {}", shape);
        SpatialArgs args = new SpatialArgs(SpatialOperation.Contains, shape);
        // SpatialArgs args = new SpatialArgs(SpatialOperation.Contains,
        // ctx.makePoint(lon, lat));
        return getRowKeyList(args, searchableField);
    }

    public List<String> getRowKeyList(SpatialArgs args, String searchableField) throws IOException {
        List<String> rowKeyList = new ArrayList<>();
        Query query = strategy.makeQuery(args);
        int limit = 1100000;
        TopDocs topDocs = searcher.search(query, limit);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc s : scoreDocs) {
            Document doc = searcher.doc(s.doc);
            rowKeyList.add(doc.get(searchableField));
        }
        return rowKeyList;
    }

}
