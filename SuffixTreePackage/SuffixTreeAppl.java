package SuffixTreePackage;

/**
 * Class with methods for carrying out applications of suffix trees
 * @author David Manlove
 */

public class SuffixTreeAppl {

	/** The suffix tree */
	private SuffixTree t;

	/**
	 * Default constructor.
	 */
	public SuffixTreeAppl () {
		t = null;
	}
	
	/**
	 * Constructor with parameter.
	 * 
	 * @param tree the suffix tree
	 */
	public SuffixTreeAppl (SuffixTree tree) {
		t = tree;
	}
	
	/**
	 * Search the suffix tree t representing string s for a target x.
	 * Stores -1 in Task1Info.pos if x is not a substring of s,
	 * otherwise stores p in Task1Info.pos such that x occurs in s
	 * starting at s[p] (p counts from 0)
	 * - assumes that characters of s and x occupy positions 0 onwards
	 * 
	 * @param x the target string to search for
	 * 
	 * @return a Task1Info object
	 */
	public Task1Info searchSuffixTree(byte[] x) {
        if (t == null || x == null || x.length == 0) {
            return new Task1Info(-1, null); 
        }

        SuffixTreeNode currentNode = t.getRoot(); 
        int index = 0;

        while (index < x.length) {
            SuffixTreeNode nextNode = t.searchList(currentNode.getChild(), x[index]);
            if (nextNode == null) {
                return new Task1Info(-1, null);
            }

            int edgeStart = nextNode.getLeftLabel();
            int edgeEnd = nextNode.getRightLabel();
            byte[] treeString = t.getString();

            for (int i = edgeStart; i <= edgeEnd && index < x.length; i++, index++) {
                if (x[index] != treeString[i]) {
                    return new Task1Info(-1, null);
                }
            }

            currentNode = nextNode; 
		}
		return new Task1Info(currentNode.getSuffix(), currentNode);
    }

	/**
	 * Search suffix tree t representing string s for all occurrences of target x.
	 * Stores in Task2Info.positions a linked list of all such occurrences.
	 * Each occurrence is specified by a starting position index in s
	 * (as in searchSuffixTree above).  The linked list is empty if there
	 * are no occurrences of x in s.
	 * - assumes that characters of s and x occupy positions 0 onwards
	 * 
	 * @param x the target string to search for
	 * 
	 * @return a Task2Info object
	 */
	public Task2Info allOccurrences(byte[] x) {
		
		SuffixTreeAppl suffixTreeAppl = new SuffixTreeAppl(t);
		Task1Info task1 = suffixTreeAppl.searchSuffixTree(x);

		SuffixTreeNode start = task1.getMatchNode();

		Task2Info task2 = new Task2Info();
		if (start == null) {
			return task2;
		} else if (start.getSuffix() == -1) {
			task2 = recLeafCount(start.getChild(), task2);
		} else {
			task2.addEntry(start.getSuffix());
		}
		return task2;
	}
	
	public Task2Info recLeafCount(SuffixTreeNode current, Task2Info task2) {
		Task2Info empty = new Task2Info();
		if (current == null) {
			return empty;
		} else if (current.getSuffix() == -1 ) {
			recLeafCount(current.getChild(), task2);
		} else {
			task2.addEntry(current.getSuffix());
		}
		recLeafCount(current.getSibling(), task2);
		return task2;
	}

    /**
     * Traverses suffix tree t representing string s and stores ln, p1 and
     * p2 in Task3Info.len, Task3Info.pos1 and Task3Info.pos2 respectively,
     * so that s[p1..p1+ln-1] = s[p2..p2+ln-1], with ln maximal;
     * i.e., finds two embeddings of a longest repeated substring of s
     * - assumes that characters of s occupy positions 0 onwards
     * so that p1 and p2 count from 0
     * 
     * @return a Task3Info object
     */
    public Task3Info traverseForLrs() {
        if (t == null) {
            return new Task3Info(0, -1, -1);
        }

        Task3Info lrsInfo = new Task3Info(0, -1, -1);
        findLrs(t.getRoot(), 0, lrsInfo);
        return lrsInfo;
    }

    /**
     * Helper method to recursively find the longest repeated substring.
     * 
     * @param node the current node
     * @param depth the current depth in the tree
     * @param lrsInfo the Task3Info object to store the result
     */
    private void findLrs(SuffixTreeNode node, int depth, Task3Info lrsInfo) {
        if (node == null) {
            return;
        }

        if (node.getChild() != null) {
            if (depth > lrsInfo.getLen()) {
                lrsInfo.setLen(depth);
                lrsInfo.setPos1(node.getChild().getSuffix());
                lrsInfo.setPos2(node.getChild().getSibling() != null ? node.getChild().getSibling().getSuffix() : -1);
            }
        }

        SuffixTreeNode child = node.getChild();
        while (child != null) {
            findLrs(child, depth + (child.getRightLabel() - child.getLeftLabel() + 1), lrsInfo);
            child = child.getSibling();
        }
    }

    /**
	 * Traverse generalised suffix tree t representing strings s1 (of length
	 * s1Length), and s2, and store ln, p1 and p2 in Task4Info.len,
	 * Task4Info.pos1 and Task4Info.pos2 respectively, so that
	 * s1[p1..p1+ln-1] = s2[p2..p2+ln-1], with len maximal;
	 * i.e., finds embeddings in s1 and s2 of a longest common substring 
         * of s1 and s2
	 * - assumes that characters of s1 and s2 occupy positions 0 onwards
	 * so that p1 and p2 count from 0
	 * 
	 * @param s1Length the length of s1
	 * 
	 * @return a Task4Info object
	 */
	public Task4Info traverseForLcs(int s1Length) {
    Task4Info task4 = new Task4Info();
    SuffixTreeNode root = t.getRoot();

    if (root == null || root.getChild() == null) {
        return task4;
    }

    recFindLcs(task4, root, 0, s1Length);
    return task4;
}

    public Task4Info recFindLcs(Task4Info task4, SuffixTreeNode current, int depth, int splitPoint) {
        if (current == null) {
            return task4;
        }

        SuffixTreeNode child = current.getChild();

        if (child.getSuffix() != -1 && child.getSibling() == null) {
            return task4;
        }

        if (child.getSuffix() == -1 && child.getSibling() == null) {
            depth += current.getRightLabel() - current.getLeftLabel() + 1;
            task4 = recFindLcs(task4, child, depth, splitPoint);
        } else {
            depth += current.getRightLabel() - current.getLeftLabel() + 1;
            task4.setString1Leaf(false);
            task4.setString2Leaf(false);
            int s1 = 0;
            int s2 = 0;

            while (child != null) {
                if (child.getSuffix() == -1) {
                    task4 = recFindLcs(task4, child, depth, splitPoint);
                }

                if (child.getLeftLabel() <= splitPoint) {
                    task4.setString1Leaf(true);
                    s1 = child.getLeftLabel();
                } else {
                    task4.setString2Leaf(true);
                    s2 = child.getLeftLabel();
                }

                child = child.getSibling();
            }

            if (task4.getString1Leaf() && task4.getString2Leaf() && task4.getLen() < depth) {
                task4.setLen(depth - 1);
                task4.setPos1(s1 - task4.getLen());
                task4.setPos2(s2 - task4.getLen() - splitPoint);
            }
        }

        return task4;
    }
}
