package com.suanfa;


import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class RBTree <T extends Comparable<T>> {
	/*
	 * 	红黑色的5个重要属性
	 * 	（1）每个节点或者是黑色，或者是红色。
	 *	（2）根节点是黑色。
	 *	（3）每个叶子节点（NIL）是黑色。 [注意：这里叶子节点，是指为空(NIL或NULL)的叶子节点！]
	 *	（4）如果一个节点是红色的，则它的子节点必须是黑色的。
	 *	（5）从一个节点到该节点的子孙节点的所有路径上包含相同数目的黑节点。
	 * 
	 * 其他要点
	 *  添加节点的时候以红色节点添加 （为啥红色 保证根节点到所有叶子节点的黑色节点数相等）
	 *  每添加一个节点至多需要三次左旋或者右旋就能达到平衡，成为一个红黑树
	 */
	volatile RBTNode<T> mRoot;//根节点
	private final static boolean RED = false; //
	private final static boolean BLACK = true;
	/*
	 * 红黑树节点
	 * 此处定义成静态内部类比较合适
	 */
	public static class RBTNode<T extends Comparable<T>> {
		boolean color; //颜色
		T key; //键值
		RBTNode<T> left;
		RBTNode<T> right;
		RBTNode<T> parent;
		public RBTNode(){

		}
		public RBTNode(T key, boolean color, RBTNode<T> left , RBTNode<T> right , RBTNode<T> parent){
			this.color = color;
			this.key = key ;
			this.left = left;
			this.right = right;
			this.parent = parent;
		}
	}
	
	
	/* 
	 * 对红黑树的节点(x)进行左旋转
	 *
	 * 左旋示意图(对节点x进行左旋)：
	 *      px                              px
	 *     /                               /
	 *    x                               y                
	 *   /  \      --(左旋)-.           / \                #
	 *  lx   y                          x  ry     
	 *     /   \                       /  \
	 *    ly   ry                     lx  ly  
	 *
	 *
	 */
	private void leftRotate(RBTNode<T> x){
		RBTNode<T> y = x.right;
		x.right = y.left;
		if(y.left != null){
			y.left.parent = x;
		}
		y.parent = x.parent; //衔接px 交换后如果X的父节点不为空
		
		if(x.parent == null){
			this.mRoot = y;
		}else{
			if(x == x.parent.left){
				x.parent.left = y;
			}else{
				x.parent.right = y;
			}
		}
		y.left = x;
		x.parent = y;
				
	}
	
	/* 
	 * 对红黑树的节点(y)进行右旋转
	 *
	 * 右旋示意图(对节点y进行左旋)：
	 *            py                               py
	 *           /                                /
	 *          y                                x                  
	 *         /  \      --(右旋)-.            /  \                     #
	 *        x   ry                           lx   y  
	 *       / \                                   / \                   #
	 *      lx  rx                                rx  ry
	 * 
	 */

	private void rightRotate(RBTNode<T> y){
		RBTNode<T> x = y.left;
		y.left = x.right;
		if(x.right != null){
			x.right.parent = y;
		}
		
		x.parent = y.parent;
		
		if(null == y.parent){
			this.mRoot = x;
		}else{
			if(y.parent.right == y){
				y.parent.right = x;
			}else{
				y.parent.left = x;
			}
		}
		x.right = y;
		y.parent = x;
	}
	
	/* 
	 * 新建结点(key)，并将其插入到红黑树中
	 *
	 * 参数说明：
	 *     key 插入结点的键值
	 */
	public void insert(T key){
		RBTNode<T> node = new RBTNode<T>(key, BLACK, null, null, null);
		if(node != null){
			insert(node);
		}
	}
	
	/* 
	 * 将结点插入到红黑树中
	 *
	 * 参数说明：
	 *     node 插入的结点        // 对应《算法导论》中的node
	 */

	private void insert(RBTNode<T> node){
		int com;
	    RBTNode<T> y = null;// 新建节点“y”，将y设为空节点。
	    RBTNode<T> x = this.mRoot; // 设“红黑树T”的根节点为“x”
	    
	 // 1. 将红黑树当作一颗二叉查找树，找到节点应该插入的地方 将节点添加到二叉查找树中。
	    while(x != null){
	    	y =x;
	    	com = node.key.compareTo(x.key);
	    	if(com < 0 ){//小于当前节点就找左边
	    		x = x.left;
	    	}else{
	    		x = x.right;
	    	}
	    }
	    
	    node.parent = y;
	    if(y == null){
	    	this.mRoot = node;//说明是红黑树第一个节点 当然就是Root节点了
	    }else{
	    	if(node.key.compareTo(y.key)<0){
	    		y.left = node;
	    	}else{
	    		y.right = node;
	    	}
	    }
	    //以红色节点添加 保证根节点到左右子树的叶子节点的黑色节点数一致
	    node.color = RED;
	    // 3. 将它重新修正为一颗二叉查找树
	    insertFixUp(node);

	}
	
	/*
	 * 红黑树插入修正函数
	 *
	 * 在向红黑树中插入节点之后(失去平衡)，再调用该函数；
	 * 目的是将它重新塑造成一颗红黑树。
	 *
	 * 参数说明：
	 *     node 插入的结点        // 对应《算法导论》中的z
	 */

	private void insertFixUp(RBTNode<T> node){
		//RBTNode<T> parent ,gparent;//父节点 和 祖父节点
		//如果当前节点的父节点是红色
		while(((parentOf(node)) != null) && isRed(node.parent)){
			//gparent = parent.parent;
			//Case 1 当前节点的父节点是红色，且当前节点的祖父节点的另一个子节点（叔叔节点）也是红色。 
			//Case 2 当前节点的父节点是红色，叔叔节点是黑色，且当前节点是其父节点的右孩子 
			//Case 3 当前节点的父节点是红色，叔叔节点是黑色，且当前节点是其父节点的左孩子
			
			if(node.parent == node.parent.parent.left){//如果当前节点父节点是祖父节点的左孩子
				//叔叔节点则为右孩子
				RBTNode<T> uncle = node.parent.parent.right;
				//case 1 条件:叔叔节点是红色
				if(null != uncle && isRed(uncle)){
					node.parent.color = BLACK; //将父节点设置成黑色
					uncle.color = BLACK;//将叔叔节点设置成黑色
					node.parent.parent.color = RED; //将祖父节点设置成红色
					node = node.parent.parent;
					//结束本次循环继续处理祖父节点及上级节点
				} else{
					//case 2 条件:叔叔节点是黑色 且当前节点是右孩子
					if( node == node.parent.right ){
						node = node.parent;
			            leftRotate(node); //左旋
					}
					
					//case 3 条件:叔叔节点是黑色 且当前节点是左孩子
					node.parent.color = BLACK;
					node.parent.parent.color = RED;
		            rightRotate(node.parent.parent); //右旋
				}    
			}else{//如果当前节点父节点是祖父节点的右孩子
				//叔叔节点则为左孩子
				RBTNode<T> uncle = node.parent.parent.left;
				//case 1 条件:叔叔节点是红色
				if(null != uncle && isRed(uncle)){
					node.parent.color = BLACK; //将父节点设置成黑色
					uncle.color = BLACK;//将叔叔节点设置成黑色
					node.parent.parent.color = RED; //将祖父节点设置成红色
					node = node.parent.parent;
					//结束本次循环继续处理祖父节点及上级节点
				} else{
					//case 2 条件:叔叔节点是黑色 且当前节点是左孩子
					if(node.parent.left == node){
						node = node.parent;
			            rightRotate(node); //右旋
					}
					//case 3 条件:叔叔节点是黑色 且当前节点是右孩子
					node.parent.color = BLACK;
					node.parent.parent.color = RED;
		            leftRotate(node.parent.parent); //左旋
				}
			}
		}
		// 将根节点设为黑色
	  this.mRoot.color= BLACK;

	}
	
	/* 
	 * 删除结点(z)，并返回被删除的结点
	 *
	 * 参数说明：
	 *     tree 红黑树的根结点
	 *     z 删除的结点
	 */
	public void remove(T key) {
	    RBTNode<T> node; 

	    if ((node = search(key)) != null)
	        remove(node);
	}
	
	/* 
	 * 删除结点(node)，并返回被删除的结点
	 *
	 * 参数说明：
	 *     node 删除的结点
	 */
	private void remove(RBTNode<T> node) {
	    RBTNode<T> child, parent;
	    boolean color;

	    // 被删除节点的"左右孩子都不为空"的情况。
	    if ( (node.left!=null) && (node.right!=null) ) {
	        // 被删节点的后继节点。(称为"取代节点")
	        // 用它来取代"被删节点"的位置，然后再将"被删节点"去掉。
	        RBTNode<T> replace = node;

	        // 获取后继节点
	        replace = replace.right;
	        while (replace.left != null){//取右边节点最小节点
	            replace = replace.left;
	        }

	        // "node节点"不是根节点(只有根节点不存在父节点)
	        if (parentOf(node)!=null) {
	            if (parentOf(node).left == node)//如果被删除节点是它父节点左儿子 
	                parentOf(node).left = replace;
	            else
	                parentOf(node).right = replace;
	        } else {
	            // "node节点"是根节点，更新根节点。 如果被删除的节点是根节点 那么他的取代变成根节点
	            this.mRoot = replace;
	        }

	        // child是"取代节点"的右孩子，也是需要"调整的节点"。
	        // "取代节点"肯定不存在左孩子！因为它是一个后继节点。
	        child = replace.right;
	        parent = parentOf(replace);
	        // 保存"取代节点"的颜色
	        color = colorOf(replace);

	        // "被删除节点"是"它的后继节点的父节点"
	        if (parent == node) {
	            parent = replace;
	        } else {
	            // child不为空
	            if (child!=null){
	                setParent(child, parent);  //将取代节点的右儿子放在取代节点移走前的位置（取代节点父节点的左儿子）
	            }
	            parent.left = child; // 设置成左儿子

	            replace.right = node.right; //  将取代节点移位到要删除节点的位置
	            setParent(node.right, replace);
	        }

	        replace.parent = node.parent;
	        replace.color = node.color;
	        replace.left = node.left;
	        node.left.parent = replace;

	        if (color == BLACK)
	            removeFixUp(child, parent);

	        node = null;
	        return ;
	    }
	    //如果被删除节点只存在一个儿子节点
	    if (node.left !=null) {
	        child = node.left;
	    } else {
	        child = node.right;
	    }

	    parent = node.parent;
	    // 保存"取代节点"的颜色
	    color = node.color;

	    if (child!=null)
	        child.parent = parent;

	    // "node节点"不是根节点
	    if (parent!=null) {
	        if (parent.left == node)
	            parent.left = child;
	        else
	            parent.right = child;
	    } else {
	        this.mRoot = child;
	    }

	    if (color == BLACK){//如果被删节点是红色的是不违反：对于任意结点而言，其到叶结点树尾端NIL指针的每条路径都包含相同数目的黑结点。  只有删除黑色的才会重新调整
	    	                //黑色节点下面两个子节点不一定是红色的 但是红色根节点下面两个节点一定要是黑色
	        removeFixUp(child, parent);
	    }
	    node = null;
	}
	
	/*
	 * 红黑树删除修正函数
	 *
	 * 在从红黑树中删除插入节点之后(红黑树失去平衡)，再调用该函数；
	 * 目的是将它重新塑造成一颗红黑树。
	 *
	 * 参数说明：
	 *     node 待修正的节点
	 */
	private void removeFixUp(RBTNode<T> node , RBTNode<T> parent){
		 RBTNode<T> other;
		 	//如果 child 不为空 且是红色 不破坏规则 就不用移动
		    while ((node==null || isBlack(node)) && (node != this.mRoot)) {
		        if (parent.left == node) {
		            other = parent.right;
		            if (isRed(other)) {
		                // Case 1: x的兄弟w是红色的  
		                setBlack(other);
		                setRed(parent);
		                leftRotate(parent);
		                other = parent.right;
		            }

		            if ((other.left==null || isBlack(other.left)) &&
		                (other.right==null || isBlack(other.right))) {
		                // Case 2: x的兄弟w是黑色，且w的俩个孩子也都是黑色的  
		                setRed(other);
		                node = parent;
		                parent = parentOf(node);
		            } else {

		                if (other.right==null || isBlack(other.right)) {
		                    // Case 3: x的兄弟w是黑色的，并且w的左孩子是红色，右孩子为黑色。  
		                    setBlack(other.left);
		                    setRed(other);
		                    rightRotate(other);
		                    other = parent.right;
		                }
		                // Case 4: x的兄弟w是黑色的；并且w的右孩子是红色的，左孩子任意颜色。
		                setColor(other, colorOf(parent));
		                setBlack(parent);
		                setBlack(other.right);
		                leftRotate(parent);
		                node = this.mRoot;
		                break;
		            }
		        } else {

		            other = parent.left;
		            if (isRed(other)) {
		                // Case 1: x的兄弟w是红色的  
		                setBlack(other);
		                setRed(parent);
		                rightRotate(parent);
		                other = parent.left;
		            }

		            if ((other.left==null || isBlack(other.left)) &&
		                (other.right==null || isBlack(other.right))) {
		                // Case 2: x的兄弟w是黑色，且w的俩个孩子也都是黑色的  
		                setRed(other);
		                node = parent;
		                parent = parentOf(node);
		            } else {

		                if (other.left==null || isBlack(other.left)) {
		                    // Case 3: x的兄弟w是黑色的，并且w的左孩子是红色，右孩子为黑色。  
		                    setBlack(other.right);
		                    setRed(other);
		                    leftRotate(other);
		                    other = parent.left;
		                }

		                // Case 4: x的兄弟w是黑色的；并且w的右孩子是红色的，左孩子任意颜色。
		                setColor(other, colorOf(parent));
		                setBlack(parent);
		                setBlack(other.left);
		                rightRotate(parent);
		                node = this.mRoot;
		                break;
		            }
		        }
		    }

		    if (node!=null)
		        setBlack(node);//如果 child 不为空 且是红色 不破坏规则 就不用移动 只用重新给他着成黑色

	}
	
	private void setParent(RBTNode<T> node , RBTNode<T> parent){
		if (node!=null){
			 node.parent = parent;
		}

	}
	
	/*
     * 获取后继节点
     *
     * node：查找该节点的后继节点 
     * flag：0，返回当前
     *       -1，查找左子数最大节点;
     *        1，查找右子数最小节点。
     */
	private RBTNode<T> searchMinOrMax(RBTNode<T> node , int flag){
		RBTNode<T> target; 
		target = node;
		if(flag == 0){
			return target;
		}else if(flag > 0){
			target = node.right;
			while(target.left!=null){
				target = target.left;
			}
		}else{
			target = node.left;
			while(target.right!=null){
				target = target.right;
			}
		}
		
		return target;
	}
	
	
	/*
	 * 从红黑树中查询 key 所对应的 node
	 */
	private RBTNode<T> search(T key){

		//目标节点
		RBTNode<T> target;
		target = mRoot;
		do{
			if(0==key.compareTo(mRoot.key)){
				return mRoot;
			}else if(key.compareTo(mRoot.key)<0){
				target = target.left;
			}else{
				target = target.right;
			}
		}while(key.compareTo(target.key)!=0);
		
		return target;
	}
	
	private void setColor(RBTNode<T> node , boolean color) {
	    	if (node!=null){
	              node.color = color;
	    	}
	}
    private void setRed(RBTNode<T> node) {
    	if (node!=null){
              node.color = RED;
    	}
    }
    
    private void setBlack(RBTNode<T> node) {
    	if (node!=null){
              node.color = BLACK;
    	}
    }
	private boolean colorOf(RBTNode<T> node) {
		return node!=null ? node.color : BLACK;
	}

	/*
	 * 获取节点的父节点
	 */
	private RBTNode<T> parentOf(RBTNode<T> node){
		if(null != node && null !=node.parent){
			return node.parent;
		}
		return null;
	}
	
	/*
	 * 当前节点是否是红色
	 */
	private boolean isRed(RBTNode<T> node){
		return ((node!=null)&&(node.color==RED)) ? true : false;
	}
	
	/*
	 * 当前节点是否是红色
	 */
	private boolean isBlack(RBTNode<T> node){
		return !isRed(node);
	}
	
	/*
     * 打印"红黑树"
     *
     * key        -- 节点的键值 
     * direction  --  0，表示该节点是根节点;
     *               -1，表示该节点是它的父结点的左孩子;
     *                1，表示该节点是它的父结点的右孩子。
     */
    private void print(RBTNode<T> tree, T key, int direction) {

        if(tree != null) {

            if(direction==0)    // tree是根节点
                System.out.printf("%2d(B) is root\n", tree.key);
            else                // tree是分支节点
                System.out.printf("%2d(%s) is %2d's %6s child\n", tree.key, isRed(tree)?"R":"B", key, direction==1?"right" : "left");

            print(tree.left, tree.key, -1);
            print(tree.right,tree.key,  1);
        }
    }

    public void print() {
        if (mRoot != null)
            print(mRoot, mRoot.key, 0);
    }

    public static void main(String[] args) {
    	
		RBTree<Integer> tree = new RBTree<Integer>();
        ConcurrentHashMap chm = new ConcurrentHashMap();
        HashMap hm = new HashMap();
        chm.put(123 ,"123");

		int a[] = {10, 40, 30, 60, 90, 70, 20, 50, 80};
		int i, ilen = a.length;
		for(i=0;i<ilen;i++){
			tree.insert(a[i]);
		}

		tree.print();
		
		tree.remove(60);
		
		tree.print();
	}
}
