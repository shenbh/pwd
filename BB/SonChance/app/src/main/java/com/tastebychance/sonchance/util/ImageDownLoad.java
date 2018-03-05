package com.tastebychance.sonchance.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tastebychance.sonchance.MyApplication;
import com.tastebychance.sonchance.R;

/**
 * 图片下载获取并且设置到控件上
 * 
 * @author Administrator
 * 
 */
public class ImageDownLoad {
	private static DisplayImageOptions options; // 设置图片显示相关参数
	static Bitmap bitmapDefault = BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.drawable.defaultphoto);;

	// 获取网络图片 根据原图比例进行缩放
	public static void getDownLoadImg(String headImgUrlString, final ImageView headImageView) {
		if (headImgUrlString != null && !"".equals(headImgUrlString)) {
			// 说明有图片了~从服务器上下载图片
			// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
			options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.defaultphoto) // 设置图片下载期间显示的图片
					.showImageForEmptyUri(R.drawable.defaultphoto) // 设置图片Uri为空或是错误的时候显示的图片
					.showImageOnFail(R.drawable.defaultphoto) // 设置图片加载或解码过程中发生错误显示的图片
					.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
					.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
					.build(); // 构建完成
			/**
			 * imageUrl 图片的Url地址 imageView 承载图片的ImageView控件 options
			 * DisplayImageOptions配置文件
			 */
			// String
			// urlImageString="http://mario.picp.net/tdd/resources/upload/";
			try {
				String a[] = headImgUrlString.split("/upload/");
				if (headImgUrlString.contains("http:")) {
					ImageLoader.getInstance().displayImage(headImgUrlString, headImageView, options, new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub

							int widths = Util.getScreenWidth();
							double temp = (double) bitmapDefault.getHeight() / bitmapDefault.getWidth();
							int heights = (int) (temp * widths);
							headImageView.getLayoutParams().width = widths;
							headImageView.getLayoutParams().height = heights;
							headImageView.setImageBitmap(bitmapDefault);
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							// TODO Auto-generated method stub
							int widths = Util.getScreenWidth();
							double temp = (double) bitmapDefault.getHeight() / bitmapDefault.getWidth();
							int heights = (int) (temp * widths);
							headImageView.getLayoutParams().width = widths;
							headImageView.getLayoutParams().height = heights;
							headImageView.setImageBitmap(bitmapDefault);
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
							// TODO Auto-generated method stub

							int widths = Util.getScreenWidth();
							double temp = (double) bitmap.getHeight() / bitmap.getWidth();
							int heights = (int) (temp * widths);
							headImageView.getLayoutParams().width = widths;
							headImageView.getLayoutParams().height = heights;
							headImageView.setImageBitmap(bitmap);
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub

						}
					});
				} else {
					ImageLoader.getInstance().displayImage(UrlManager.REQUESTIMGURL + headImgUrlString, headImageView, options, new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub
							int widths = Util.getScreenWidth();
							double temp = (double) bitmapDefault.getHeight() / bitmapDefault.getWidth();
							int heights = (int) (temp * widths);


							if (null == headImageView.getLayoutParams()){
								LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(widths, heights);
								headImageView.setLayoutParams(lp);
							}else{
								headImageView.getLayoutParams().width = widths;
								headImageView.getLayoutParams().height = heights;
							}
							headImageView.setImageBitmap(bitmapDefault);
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							// TODO Auto-generated method stub
							int widths = Util.getScreenWidth();
							double temp = (double) bitmapDefault.getHeight() / bitmapDefault.getWidth();
							int heights = (int) (temp * widths);
							headImageView.getLayoutParams().width = widths;
							headImageView.getLayoutParams().height = heights;
							headImageView.setImageBitmap(bitmapDefault);
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
							// TODO Auto-generated method stub

							int widths = Util.getScreenWidth();
							double temp = (double) bitmap.getHeight() / bitmap.getWidth();
							int heights = (int) (temp * widths);
							headImageView.getLayoutParams().width = widths;
							headImageView.getLayoutParams().height = heights;
							headImageView.setImageBitmap(bitmap);
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub

						}
					});
				}

			} catch (Exception e) {
				// TODO: handle exception	
				e.printStackTrace();

			}
		} else {
			int widths = Util.getScreenWidth();
			double temp = (double) bitmapDefault.getHeight() / bitmapDefault.getWidth();
			int heights = (int) (temp * widths);
			headImageView.getLayoutParams().width = widths;
			headImageView.getLayoutParams().height = heights;
			headImageView.setImageBitmap(bitmapDefault);
		}
	}

	// 获取网络图片 变成圆形
	public static void getDownLoadCircleImg(String headImgUrlString, final ImageView headImageView) {

		if (headImgUrlString != null && !"".equals(headImgUrlString)) {
			// 说明有图片了~从服务器上下载图片
			// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
			options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.defaultphoto) // 设置图片下载期间显示的图片
					.showImageForEmptyUri(R.drawable.defaultphoto) // 设置图片Uri为空或是错误的时候显示的图片
					.showImageOnFail(R.drawable.defaultphoto) // 设置图片加载或解码过程中发生错误显示的图片
					.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
					.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中

					.build(); // 构建完成
			/**
			 * imageUrl 图片的Url地址 imageView 承载图片的ImageView控件 options
			 * DisplayImageOptions配置文件
			 */
			// String
			// urlImageString="http://mario.picp.net/tdd/resources/upload/";
			try {
				String a[] = headImgUrlString.split("/upload/");
				if (headImgUrlString.contains("http:")) {
					ImageLoader.getInstance().displayImage(headImgUrlString, headImageView, options, new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {

							headImageView.setImageBitmap(UiHelper.CircleImageView(bitmap, 2));
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
						}
					});
				} else {
					ImageLoader.getInstance().displayImage(UrlManager.REQUESTIMGURL + headImgUrlString, headImageView, options, new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
							headImageView.setImageBitmap(UiHelper.CircleImageView(bitmap, 2));
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
						}
					});
				}
			} catch (Exception e) {
				// TODO: handle exception	
				e.printStackTrace();

			}
		} else {
			headImageView.setImageBitmap(bitmapDefault);
		}
	}

	// 获取网络图片 变成圆形 下载过程中没有图片--头像专用（url不一样）
	public static void getDownLoadCircleImgNoWaitPic(String url, final ImageView imageView) {
		if (url != null && !"".equals(url)) {
			// 说明有图片了~从服务器上下载图片 使用DisplayImageOptions.Builder()创建DisplayImageOptions
			options = new DisplayImageOptions.Builder()//.showImageOnLoading(R.drawable.defaultphoto) // 设置图片下载期间显示的图片
					.showImageForEmptyUri(R.drawable.defaultphoto) // 设置图片Uri为空或是错误的时候显示的图片
					.showImageOnFail(R.drawable.defaultphoto) // 设置图片加载或解码过程中发生错误显示的图片
					.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
					.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
					.build(); // 构建完成
			/**
			 * imageUrl 图片的Url地址 imageView 承载图片的ImageView控件 options
			 * DisplayImageOptions配置文件
			 */
			try {
				String a[] = url.split("/upload/");
				if (url.contains("http:")) {
					ImageLoader.getInstance().displayImage(url, imageView, options, new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {

							imageView.setImageBitmap(UiHelper.CircleImageView(bitmap, 2));
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
						}
					});
				} else {
					ImageLoader.getInstance().displayImage((Constants.IS_DEVELOPING? UrlManager.REQUESTURL_HEAD_TEST : UrlManager.REQUESTIMGURL) + url, imageView, options, new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
							imageView.setImageBitmap(UiHelper.CircleImageView(bitmap, 2));
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
						}
					});
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();

			}
		} else {
			imageView.setImageBitmap(bitmapDefault);
		}
	}

	// 获取网络图片 大小固定
	public static void getDownLoadSmallImg(String headImgUrlString, final ImageView headImageView) {

		if (headImgUrlString != null && !"".equals(headImgUrlString)) {
			// 说明有图片了~从服务器上下载图片
			// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
			options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.defaultphoto) // 设置图片下载期间显示的图片
					.showImageForEmptyUri(R.drawable.defaultphoto) // 设置图片Uri为空或是错误的时候显示的图片
					.showImageOnFail(R.drawable.defaultphoto) // 设置图片加载或解码过程中发生错误显示的图片
					.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
					.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
					.build(); // 构建完成
			/**
			 * imageUrl 图片的Url地址 imageView 承载图片的ImageView控件 options
			 * DisplayImageOptions配置文件
			 */
			// String
			// urlImageString="http://mario.picp.net/tdd/resources/upload/";
			try {
				String a[] = headImgUrlString.split("/upload/");
				if (headImgUrlString.contains("http:") || headImgUrlString.contains("https:")) {
					ImageLoader.getInstance().displayImage(headImgUrlString, headImageView, options);
				} else {
					ImageLoader.getInstance().displayImage(UrlManager.REQUESTIMGURL + headImgUrlString, headImageView, options);
					System.out.println("UrlManager.REQUESTIMGURL + headImgUrlString -------------------------= " + UrlManager.REQUESTIMGURL + headImgUrlString);
				}
			} catch (Exception e) {

			}
		} else {
			headImageView.setImageBitmap(bitmapDefault);
		}
	}

	/*// 获取网络压缩后的图片，用来显示在list列表上的
	public static void getDownLoadPressImg(String headImgUrlString, final ImageView headImageView) {

		if (headImgUrlString != null && !"".equals(headImgUrlString)) {
			// 说明有图片了~从服务器上下载图片
			// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
			options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.defaultphoto) // 设置图片下载期间显示的图片
					.showImageForEmptyUri(R.drawable.defaultphoto) // 设置图片Uri为空或是错误的时候显示的图片
					.showImageOnFail(R.drawable.defaultphoto) // 设置图片加载或解码过程中发生错误显示的图片
					.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
					.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
					.build(); // 构建完成
			*//**
			 * imageUrl 图片的Url地址 imageView 承载图片的ImageView控件 options
			 * DisplayImageOptions配置文件
			 *//*
			// String
			// urlImageString="http://mario.picp.net/tdd/resources/upload/";
			try {
				String a[] = headImgUrlString.split("/upload/");
				if (headImgUrlString.contains("http:")) {
					ImageLoader.getInstance().displayImage(headImgUrlString, headImageView, options);
				} else {
					ImageLoader.getInstance().displayImage(UrlManager.minImgUrl + headImgUrlString, headImageView, options);

				}
			} catch (Exception e) {

			}
		} else {
			headImageView.setImageBitmap(bitmapDefault);
		}
	}
*/



}
